package com.example.moontrade.data.ws

import android.util.Log
import com.example.moontrade.model.OrderBookLevel
import com.example.moontrade.model.OrderBookMessage
import com.example.moontrade.model.OrderBookSnapshot
import com.example.moontrade.model.TradeMatch
import com.example.moontrade.model.WebSocketStatus
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.*

private const val ORDERBOOK_WS_BASE = "ws://insectivora.eu:1010/ws/book/"
private const val ORDERBOOK_TAG = "OrderBookWS"

class OrderBookWebSocket(
    private val client: OkHttpClient,
    private val scope: CoroutineScope
) {

    @Volatile
    private var socket: WebSocket? = null

    @Volatile
    private var symbol: String? = null

    @Volatile
    private var connectionId: Long = 0L

    private val moshi = Moshi.Builder()
        .add(
            PolymorphicJsonAdapterFactory.of(OrderBookMessage::class.java, "type")
                // FULL snapshot variants
                .withSubtype(OrderBookMessage.Full::class.java, "full")
                .withSubtype(OrderBookMessage.Full::class.java, "FullSnapshotDto")
                // DELTA variants
                .withSubtype(OrderBookMessage.Delta::class.java, "delta")
                .withSubtype(OrderBookMessage.Delta::class.java, "DeltaDto")
        )
        .add(KotlinJsonAdapterFactory())
        .build()

    private val adapter = moshi.adapter(OrderBookMessage::class.java)

    private var localBids = mutableMapOf<Double, Double>()
    private var localAsks = mutableMapOf<Double, Double>()
    private var localTrades = mutableListOf<TradeMatch>()

    private val _snapshot = MutableStateFlow<OrderBookSnapshot?>(null)
    val snapshot: StateFlow<OrderBookSnapshot?> = _snapshot

    private val _status = MutableStateFlow<WebSocketStatus>(WebSocketStatus.Idle)
    val status: StateFlow<WebSocketStatus> = _status

    fun connect(newSymbol: String) {
        val normalized = newSymbol.uppercase()
        Log.d(ORDERBOOK_TAG, "connect(symbol=$normalized)")

        if (symbol == normalized && socket != null) {
            Log.d(ORDERBOOK_TAG, "Already connected to $normalized → skip")
            return
        }

        symbol = normalized
        resetLocal()

        // новая генерация, старые колбэки становятся неактуальными
        val myId = ++connectionId

        socket?.close(1000, "switch to $normalized")
        socket = null
        _status.value = WebSocketStatus.Connecting

        val request = Request.Builder()
            .url("$ORDERBOOK_WS_BASE$normalized")
            .build()

        val listener = object : WebSocketListener() {

            override fun onOpen(ws: WebSocket, response: Response) {
                if (myId != connectionId) {
                    Log.d(ORDERBOOK_TAG, "onOpen stale (id=$myId), closing ws")
                    ws.close(1000, "stale")
                    return
                }
                Log.d(ORDERBOOK_TAG, "✅ OrderBook WS opened for $normalized (id=$myId)")
                socket = ws
                _status.value = WebSocketStatus.Connected
            }

            override fun onMessage(ws: WebSocket, text: String) {
                if (myId != connectionId) {
                    Log.d(ORDERBOOK_TAG, "onMessage stale (id=$myId), ignore")
                    return
                }

                val current = symbol
                if (current == null || current != normalized || ws != socket) {
                    Log.d(ORDERBOOK_TAG, "Ignoring stale WS message for $normalized (current=$current)")
                    return
                }

                Log.d(ORDERBOOK_TAG, "← WS message for $normalized (len=${text.length})")

                scope.launch(Dispatchers.Default) {
                    val msg = try {
                        adapter.fromJson(text)
                    } catch (e: Exception) {
                        Log.e(ORDERBOOK_TAG, "Failed to parse WS message", e)
                        null
                    } ?: return@launch

                    when (msg) {
                        is OrderBookMessage.Full  -> applyFull(msg)
                        is OrderBookMessage.Delta -> applyDelta(msg)
                    }
                }
            }

            override fun onClosed(ws: WebSocket, code: Int, reason: String) {
                if (myId != connectionId) {
                    Log.d(ORDERBOOK_TAG, "onClosed stale (id=$myId), ignore")
                    return
                }
                Log.d(ORDERBOOK_TAG, "🛑 OrderBook WS closed for $normalized: $code / $reason")
                if (socket == ws) socket = null
                _status.value = WebSocketStatus.Disconnected(reason)
            }

            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                if (myId != connectionId) {
                    Log.d(ORDERBOOK_TAG, "onFailure stale (id=$myId), ignore")
                    return
                }
                Log.e(ORDERBOOK_TAG, "💥 OrderBook WS failure for $normalized: ${t.message}", t)
                if (socket == ws) socket = null
                _status.value = WebSocketStatus.Error(t.message ?: "OrderBook WS failure")
            }
        }

        client.newWebSocket(request, listener)
    }

    fun disconnect() {
        Log.d(ORDERBOOK_TAG, "disconnect()")
        connectionId++
        socket?.close(1000, "disconnect")
        socket = null
        symbol = null
        resetLocal()
        _status.value = WebSocketStatus.Idle
    }

    private fun resetLocal() {
        localBids.clear()
        localAsks.clear()
        localTrades.clear()
        _snapshot.value = null
    }

    private fun applyFull(msg: OrderBookMessage.Full) {
        localBids = msg.bids.associate { it.price to it.volume }.toMutableMap()
        localAsks = msg.asks.associate { it.price to it.volume }.toMutableMap()
        localTrades = msg.matches.toMutableList()

        _snapshot.value = OrderBookSnapshot(
            bids = msg.bids,
            asks = msg.asks,
            matches = msg.matches
        )
    }

    private fun applyDelta(msg: OrderBookMessage.Delta) {
        // BIDS
        for (lvl in msg.bid_updates) {
            if (lvl.volume == 0.0) localBids.remove(lvl.price)
            else localBids[lvl.price] = lvl.volume
        }

        // ASKS
        for (lvl in msg.ask_updates) {
            if (lvl.volume == 0.0) localAsks.remove(lvl.price)
            else localAsks[lvl.price] = lvl.volume
        }

        // TRADES
        if (msg.trade_updates.isNotEmpty()) {
            localTrades.addAll(0, msg.trade_updates)
            localTrades = localTrades.take(20).toMutableList()
        }

        _snapshot.value = OrderBookSnapshot(
            bids = localBids.toList()
                .sortedByDescending { it.first }
                .map { OrderBookLevel(it.first, it.second) },

            asks = localAsks.toList()
                .sortedBy { it.first }
                .map { OrderBookLevel(it.first, it.second) },

            matches = localTrades
        )
    }
}
