package com.example.moontrade.data.ws

import android.util.Log
import com.example.moontrade.model.OrderBookSnapshot
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.*
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

    // --------------------------
    //  MOSHI JSON ADAPTER
    // --------------------------
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val adapter: JsonAdapter<OrderBookSnapshot> =
        moshi.adapter(OrderBookSnapshot::class.java)

    // --------------------------
    //  PUBLIC API
    // --------------------------
    fun connect(symbol: String) {
        disconnect()

        //Important clear the old snapshot
        _snapshot.value = null
        val request = Request.Builder()
            .url("$WS_URL_BASE${symbol.uppercase()}")
            .build()

        socket = client.newWebSocket(request, Listener())
    }

    fun disconnect() {
        socket?.cancel()
        socket = null
    }

    // --------------------------
    //  LISTENER
    // --------------------------
    private inner class Listener : WebSocketListener() {

        override fun onOpen(ws: WebSocket, response: Response) {
            Log.d(TAG, "✅ WS opened for $WS_URL_BASE")
        }

        override fun onMessage(ws: WebSocket, text: String) {
            Log.d(TAG, "← WS received snapshot")

            scope.launch {
                val snapshot = adapter.fromJson(text)
                if (snapshot != null) {
                    _snapshot.value = snapshot
                } else {
                    Log.e(TAG, "❌ Failed to parse snapshot (null result)")
                }
            }
        }

        override fun onClosed(ws: WebSocket, code: Int, reason: String) {
            Log.d(TAG, "🛑 WS closed: $code — $reason")
        }

        override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
            Log.e(TAG, "💥 WS failure", t)
            response?.let { Log.e(TAG, "💥 WS response: $it") }
        }
    }
}
