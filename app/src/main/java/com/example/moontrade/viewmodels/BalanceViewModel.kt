// app/src/main/java/com/example/moontrade/viewmodels/BalanceViewModel.kt
package com.example.moontrade.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moontrade.auth.AuthPreferences
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.*
import org.json.JSONObject
import javax.inject.Inject

private const val TAG    = "BalanceWS"
private const val WS_URL = "ws://10.0.2.2:3000/ws/balance"

@HiltViewModel
class BalanceViewModel @Inject constructor(
    private val prefs: AuthPreferences
) : ViewModel() {

    /* ---------- public state ---------- */

    private val _balance = MutableStateFlow("Loading…")
    val  balance: StateFlow<String> = _balance.asStateFlow()

    /* ---------- internals ---------- */

    private val okHttp  = OkHttpClient()
    private var socket: WebSocket? = null
    private var currentMode: Mode  = Mode.Main

    /* ---------- API ---------- */

    fun connect(initialMode: Mode) = viewModelScope.launch(Dispatchers.IO) {
        Log.d(TAG, "connect()")

        // 1. get (or refresh) id-token --------------------------------------
        var idToken = prefs.getIdToken()
        if (idToken.isNullOrBlank()) {
            FirebaseAuth.getInstance().currentUser?.let { user ->
                idToken = user.getIdToken(false).await().token
                idToken?.let { prefs.saveIdToken(it) }
            }
        }

        if (idToken.isNullOrBlank()) {
            Log.e(TAG, "Id-token is still null – abort")
            _balance.value = "Not logged in"
            return@launch
        }

        // 2. open WS ---------------------------------------------------------
        currentMode = initialMode

        socket = okHttp.newWebSocket(
            Request.Builder().url(WS_URL).build(),
            WsListener(idToken!!)
        )
    }

    fun changeMode(mode: Mode) {
        currentMode = mode
        val payload = JSONObject()
            .put("type", "changeMode")
            .put("mode", buildModeJson(mode))
        Log.d(TAG, "→ $payload")
        socket?.send(payload.toString())
    }

    override fun onCleared() {
        socket?.close(1000, "viewModel cleared")
    }

    /* ---------- helpers ---------- */

    private fun buildSubscribePayload(token: String): String =
        JSONObject()
            .put("type", "subscribe")
            .put("id_token", token)
            .put("mode", buildModeJson(currentMode))
            .toString()

    private fun buildModeJson(mode: Mode): Any = when (mode) {
        is Mode.Main       -> "main"
        is Mode.Tournament -> JSONObject()
            .put("tournament", JSONObject().put("tournament_id", mode.tournamentId))
    }

    /* ---------- WebSocket listener ---------- */

    private inner class WsListener(private val token: String) : WebSocketListener() {

        override fun onOpen(ws: WebSocket, resp: Response) {
            Log.d(TAG, "WS opened")
            ws.send(buildSubscribePayload(token))
        }

        override fun onMessage(ws: WebSocket, text: String) {
            Log.d(TAG, "← $text")
            runCatching {
                val bal = JSONObject(text).getString("balance")
                _balance.value = "$bal USDT"
            }
        }

        override fun onFailure(ws: WebSocket, t: Throwable, r: Response?) {
            Log.e(TAG, "WS error", t)
            _balance.value = "WS error: ${t.message}"
        }

        override fun onClosed(ws: WebSocket, code: Int, reason: String) {
            Log.d(TAG, "WS closed ($code) $reason")
        }
    }

    /* ---------- public Mode ---------- */

    sealed class Mode {
        object Main : Mode()
        data class Tournament(val tournamentId: String) : Mode()
    }
}
