package com.example.moontrade.data.ws

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.*
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG    = "BalanceWS"
private const val WS_URL = "ws://10.0.2.2:3000/ws/balance"

@Singleton
class WebSocketManager @Inject constructor() {

    sealed class Mode {
        data object Main : Mode()
        data class Tournament(val id: String) : Mode()
    }

    /* ——— state ——— */
    private val _balance = MutableStateFlow("Loading…")
    val balance: StateFlow<String> = _balance

    /* ——— internals ——— */
    private val client = OkHttpClient()
    private var socket: WebSocket? = null
    private var currentMode: Mode = Mode.Main
    private var idToken: String = ""

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    /* ——— public API ——— */

    fun connect(token: String, mode: Mode) {
        idToken     = token
        currentMode = mode
        socket = client.newWebSocket(
            Request.Builder().url(WS_URL).build(),
            Listener()
        )
    }

    fun changeMode(mode: Mode) {
        currentMode = mode
        send(
            JSONObject()
                .put("type", "changeMode")
                .put("mode", mode.toJson())
        )
    }

    fun disconnect() = socket?.close(1000, "scope finished")

    /* ——— helpers ——— */

    private fun send(j: JSONObject) {
        Log.d(TAG, "→ $j")
        socket?.send(j.toString())
    }

    private fun Mode.toJson(): Any = when (this) {
        is Mode.Main       -> "main"
        is Mode.Tournament -> JSONObject()
            .put("tournament", JSONObject().put("tournament_id", id))
    }

    /* ——— listener ——— */

    private inner class Listener : WebSocketListener() {

        override fun onOpen(ws: WebSocket, resp: Response) {
            send(
                JSONObject()
                    .put("type", "subscribe")
                    .put("id_token", idToken)
                    .put("mode", currentMode.toJson())
            )
        }

        override fun onMessage(ws: WebSocket, text: String) {
            Log.d(TAG, "← $text")
            runCatching {
                val j   = JSONObject(text)
                val bal = when {
                    j.has("balance") -> j.get("balance")
                    j.optJSONObject("payload")?.has("balance") == true ->
                        j.getJSONObject("payload").get("balance")
                    else -> return
                }
                scope.launch { _balance.value = "$bal USDT" }
            }.onFailure { Log.e(TAG, "parse error", it) }
        }

        override fun onClosed(ws: WebSocket, code: Int, reason: String) {
            Log.d(TAG, "WS closed → code=$code reason=$reason")
        }
        override fun onFailure(ws: WebSocket, t: Throwable, r: Response?) {
            Log.e(TAG, "WS error", t)
            scope.launch { _balance.value = "WS error: ${t.message}" }
        }
    }
}
