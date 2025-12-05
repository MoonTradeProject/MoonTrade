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

private const val BALANCE_WS_URL = "ws://insectivora.eu:1010/ws/balance"
private const val BALANCE_TAG = "BalanceWS"

class BalanceWebSocket(
    private val client: OkHttpClient,
    private val scope: CoroutineScope
) {

    @Volatile
    private var socket: WebSocket? = null

    @Volatile
    private var connectionId: Long = 0L

    private val gson = Gson()

    @Volatile
    private var token: String? = null

    @Volatile
    private var mode: Mode = Mode.Main

    private val _balance = MutableStateFlow("Loading…")
    val balance: StateFlow<String> = _balance

    private val _roi = MutableStateFlow("–")
    val roi: StateFlow<String> = _roi

    private val _status = MutableStateFlow<WebSocketStatus>(WebSocketStatus.Idle)
    val status: StateFlow<WebSocketStatus> = _status

    fun connect(newToken: String, newMode: Mode) {
        Log.d(BALANCE_TAG, "connect(token, mode=$newMode)")

        token = newToken
        mode = newMode

        // Увеличиваем generation, все старые колбэки игнорятся
        val myId = ++connectionId

        socket?.cancel()
        socket = null
        _status.value = WebSocketStatus.Connecting

        val request = Request.Builder()
            .url(BALANCE_WS_URL)
            .build()

        val listener = object : WebSocketListener() {

            override fun onOpen(ws: WebSocket, response: Response) {
                if (myId != connectionId) {
                    Log.d(BALANCE_TAG, "onOpen: stale connection, closing")
                    ws.close(1000, "stale")
                    return
                }

                Log.d(BALANCE_TAG, "✅ Balance WS opened (id=$myId)")
                socket = ws
                _status.value = WebSocketStatus.Connected

                scope.launch {
                    delay(50)
                    val t = token ?: return@launch

                    val msg = JsonObject().apply {
                        addProperty("type", "subscribe")
                        addProperty("id_token", t)
                        add("mode", mode.toJson())
                    }.toString()

                    val ok = ws.send(msg)
                    if (!ok) {
                        Log.w(BALANCE_TAG, "Failed to send subscribe message")
                    }
                }
            }

            override fun onMessage(ws: WebSocket, text: String) {
                if (myId != connectionId) {
                    Log.d(BALANCE_TAG, "onMessage: stale (id=$myId), ignore")
                    return
                }

                Log.d(BALANCE_TAG, "← Balance WS message: $text")

                scope.launch(Dispatchers.Default) {
                    runCatching {
                        gson.fromJson(text, BalanceResponse::class.java)
                    }.onSuccess { data ->
                        withContext(Dispatchers.Main) {
                            _balance.value = "${data.balance} USDT"
                            _roi.value = data.roi?.let { "%.2f%%".format(it) } ?: "–"
                        }
                    }.onFailure {
                        Log.e(BALANCE_TAG, "❌ Failed to parse balance WS message", it)
                    }
                }
            }

            override fun onClosed(ws: WebSocket, code: Int, reason: String) {
                if (myId != connectionId) {
                    Log.d(BALANCE_TAG, "onClosed: stale (id=$myId) -> ignore")
                    return
                }
                Log.d(BALANCE_TAG, "🛑 Balance WS closed: $code / $reason")
                socket = null
                _status.value = WebSocketStatus.Disconnected(reason)
            }

            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                if (myId != connectionId) {
                    Log.d(BALANCE_TAG, "onFailure: stale (id=$myId) -> ignore")
                    return
                }
                Log.e(BALANCE_TAG, "💥 Balance WS failure: ${t.message}", t)
                socket = null
                _status.value = WebSocketStatus.Error(t.message ?: "Balance WS failure")
            }
        }

        client.newWebSocket(request, listener)
    }

    fun disconnect() {
        Log.d(BALANCE_TAG, "disconnect()")
        connectionId++ // всё старое становится "stale"
        socket?.close(1000, "disconnect")
        socket = null
        _status.value = WebSocketStatus.Idle
    }

    fun changeMode(newMode: Mode) {
        Log.d(BALANCE_TAG, "changeMode(newMode=$newMode)")
        mode = newMode

        val msg = JsonObject().apply {
            addProperty("type", "changeMode")
            add("mode", newMode.toJson())
        }.toString()

        val ok = socket?.send(msg) ?: false
        if (!ok) {
            Log.w(BALANCE_TAG, "changeMode(): socket is not open, message not sent")
        }
    }
}
