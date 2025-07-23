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
import java.net.SocketException
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "BalanceWS"
private const val WS_URL = "ws://10.0.2.2:3000/ws/balance"

@Singleton
class WebSocketManager @Inject constructor() {

    private val gson = Gson()
    private val client = OkHttpClient()
    private var socket: WebSocket? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _balance = MutableStateFlow("Loading…")
    val balance: StateFlow<String> = _balance

    private val _roi = MutableStateFlow("–")
    val roi: StateFlow<String> = _roi

    private val _status = MutableStateFlow<WebSocketStatus>(WebSocketStatus.Idle)
    val status: StateFlow<WebSocketStatus> = _status

    private val currentMode = AtomicReference<Mode>(Mode.Main)
    private val idTokenRef = AtomicReference<String>()
    private var userClosed = false

    private var onConnected: (() -> Unit)? = null

    fun connect(token: String, mode: Mode, onConnectedCallback: () -> Unit) {
        if (socket != null) {
            Log.w(TAG, "⚠️ Already connected — skipping connect()")
            return
        }

        Log.d(TAG, "⚙️ Opening new WebSocket with mode=$mode")
        _status.value = WebSocketStatus.Connecting

        userClosed = false
        idTokenRef.set(token)
        currentMode.set(mode)
        onConnected = onConnectedCallback

        socket = client.newWebSocket(Request.Builder().url(WS_URL).build(), Listener())
    }

    fun sendChangeMode(mode: Mode) {
        currentMode.set(mode)
        val json = JsonObject().apply {
            addProperty("type", "changeMode")
            add("mode", mode.toJson())
        }
        send(json.toString())
    }

    fun disconnect() {
        userClosed = true
        socket?.cancel()
        socket = null
        _status.value = WebSocketStatus.Idle
    }

    private fun send(json: String) {
        Log.d(TAG, "→ $json")
        socket?.send(json) ?: Log.w(TAG, "⚠️ Tried to send but socket is null")
    }

    private inner class Listener : WebSocketListener() {
        override fun onOpen(ws: WebSocket, response: Response) {
            Log.d(TAG, "✅ WebSocket opened")
            _status.value = WebSocketStatus.Connected
            onConnected?.invoke()

            scope.launch {
                delay(50)
                val subscribe = JsonObject().apply {
                    addProperty("type", "subscribe")
                    addProperty("id_token", idTokenRef.get())
                    add("mode", currentMode.get().toJson())
                }
                send(subscribe.toString())
            }
        }

        override fun onMessage(ws: WebSocket, text: String) {
            Log.d(TAG, "← $text")
            runCatching {
                val res = gson.fromJson(text, BalanceResponse::class.java)
                scope.launch {
                    _balance.value = "${res.balance} USDT"
                    _roi.value = res.roi?.let { "${"%.2f".format(it)}%" } ?: "–"
                }
            }.onFailure {
                Log.e(TAG, "❌ Failed to parse balance", it)
                _status.value = WebSocketStatus.Error("Invalid response")
            }
        }

        override fun onClosed(ws: WebSocket, code: Int, reason: String) {
            Log.d(TAG, "🛑 Closed: $code — $reason")
            socket = null
            _status.value = WebSocketStatus.Idle
        }

        override fun onFailure(ws: WebSocket, t: Throwable, r: Response?) {
            if (userClosed || t is SocketException && t.message?.contains("Socket closed") == true) {
                Log.w(TAG, "⛔ Ignoring socket closed by user")
                return
            }

            Log.e(TAG, "💥 WS failure", t)
            _status.value = WebSocketStatus.Error(t.message ?: "Unknown error")

            scope.launch {
                delay(3000)
                Log.d(TAG, "🔄 Reconnect scheduled")
                val token = idTokenRef.get()
                val mode = currentMode.get()
                connect(token, mode) { onConnected?.invoke() }
            }
        }
    }
}
