package com.example.moontrade.data.ws

import android.util.Log
import com.example.moontrade.model.BalanceResponse
import com.example.moontrade.model.Mode
import com.example.moontrade.model.WebSocketStatus
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.*
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "BalanceWS"
private const val WS_URL = "ws://insectivora.eu:1010/ws/balance"

@Singleton
class WebSocketManager @Inject constructor() {

    private val gson = Gson()
    private val client = OkHttpClient()

    private var socket: WebSocket? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // UI streams
    private val _balance = MutableStateFlow("Loading…")
    val balance: StateFlow<String> = _balance

    private val _roi = MutableStateFlow("–")
    val roi: StateFlow<String> = _roi

    private val _status = MutableStateFlow<WebSocketStatus>(WebSocketStatus.Idle)
    val status: StateFlow<WebSocketStatus> = _status

    private var savedToken: String? = null
    private var savedMode: Mode = Mode.Main

    /**
     * Connect: simple, no reconnect logic.
     * Always cancels the old socket and opens a new one.
     */
    fun connect(token: String, mode: Mode) {
        Log.d(TAG, "⚙️ connect(token, mode=$mode)")

        savedToken = token
        savedMode = mode

        // Always close previous WS
        socket?.cancel()
        socket = null

        _status.value = WebSocketStatus.Connecting

        val request = Request.Builder().url(WS_URL).build()
        socket = client.newWebSocket(request, Listener())
    }

    fun disconnect() {
        Log.d(TAG, "🔌 disconnect() called")
        socket?.cancel()
        socket = null
        _status.value = WebSocketStatus.Idle
    }

    fun sendChangeMode(mode: Mode) {
        savedMode = mode
        send(
            JsonObject().apply {
                addProperty("type", "changeMode")
                add("mode", mode.toJson())
            }.toString()
        )
    }

    private fun send(json: String) {
        val ok = socket?.send(json) ?: false
        if (!ok) Log.w(TAG, "⚠️ Socket is not open for send()")
    }

    private fun sendSubscribe() {
        val token = savedToken ?: return

        val msg = JsonObject().apply {
            addProperty("type", "subscribe")
            addProperty("id_token", token)
            add("mode", savedMode.toJson())
        }

        send(msg.toString())
    }

    private inner class Listener : WebSocketListener() {

        override fun onOpen(ws: WebSocket, response: Response) {
            Log.d(TAG, "✅ WebSocket opened")
            _status.value = WebSocketStatus.Connected

            scope.launch {
                delay(50)
                sendSubscribe()
            }
        }

        override fun onMessage(ws: WebSocket, text: String) {
            Log.d(TAG, "← WS message: $text")

            runCatching {
                val res = gson.fromJson(text, BalanceResponse::class.java)

                scope.launch(Dispatchers.Main) {
                    _balance.value = "${res.balance} USDT"
                    _roi.value = res.roi?.let { "%.2f%%".format(it) } ?: "–"
                }
            }.onFailure {
                Log.e(TAG, "❌ invalid WS message", it)
            }
        }

        override fun onClosed(ws: WebSocket, code: Int, reason: String) {
            Log.d(TAG, "🛑 Closed: $reason")
            _status.value = WebSocketStatus.Disconnected(reason)
        }

        override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
            Log.e(TAG, "💥 WS failure: ${t.message}", t)
            socket = null
            _status.value = WebSocketStatus.Error(t.message ?: "WS failure")
        }
    }
}
