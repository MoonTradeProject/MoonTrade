package com.example.moontrade.data.ws
import android.util.Log
import com.example.moontrade.model.OrderBookLevel
import com.example.moontrade.model.OrderBookSnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONArray
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "OrderBookWS"
private const val WS_URL_BASE = "ws://insectivora.eu:1010/ws/book/"


@Singleton
class OrderBookWebSocketManager @Inject constructor() {

    private val client = OkHttpClient()
    private var socket: WebSocket? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _snapshot = MutableStateFlow<OrderBookSnapshot?>(null)
    val snapshot: StateFlow<OrderBookSnapshot?> = _snapshot

    fun connect(symbol: String) {
        disconnect() // Ensure clean state

        val request = Request.Builder()
            .url("$WS_URL_BASE${symbol.uppercase()}")
            .build()

        socket = client.newWebSocket(request, Listener())
    }

    fun disconnect() {
        socket?.cancel()
        socket = null
    }

    private inner class Listener : WebSocketListener() {
        override fun onOpen(ws: WebSocket, response: Response) {
            Log.d(TAG, "✅ WS opened for order book")
        }

        override fun onMessage(ws: WebSocket, text: String) {
            Log.d(TAG, "← Received: $text")

            runCatching {
                val jsonArray = JSONArray(text)
                val bidsJson = jsonArray.getJSONArray(0)
                val asksJson = jsonArray.getJSONArray(1)

                val bids = List(bidsJson.length()) { i ->
                    val level = bidsJson.getJSONArray(i)
                    OrderBookLevel(level.getDouble(0), level.getDouble(1))
                }
                val asks = List(asksJson.length()) { i ->
                    val level = asksJson.getJSONArray(i)
                    OrderBookLevel(level.getDouble(0), level.getDouble(1))
                }

                scope.launch {
                    Log.d(TAG, "✅ Parsed snapshot: bids=${bids.size}, asks=${asks.size}")
                    _snapshot.value = OrderBookSnapshot(bids, asks)
                }
            }.onFailure {
                Log.e(TAG, "❌ Failed to parse WS order book", it)
            }
        }

        override fun onClosed(ws: WebSocket, code: Int, reason: String) {
            Log.d(TAG, "🛑 WS closed: $code — $reason")
        }

        override fun onFailure(ws: WebSocket, t: Throwable, r: Response?) {
            Log.e(TAG, "💥 WS error", t)
            r?.let {
                Log.e(TAG, "💥 WS response: $it")
            }
        }
    }

}
