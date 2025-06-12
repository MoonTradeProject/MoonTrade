package com.example.moontrade.session

import android.util.Base64
import android.util.Log
import com.example.moontrade.auth.AuthPreferences
import com.example.moontrade.data.ws.WebSocketManager
import com.example.moontrade.model.Mode
import com.example.moontrade.model.WebSocketStatus
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import org.json.JSONObject
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "SessionManager"

@Singleton
class SessionManager @Inject constructor(
    private val prefs: AuthPreferences,
    private val ws: WebSocketManager
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _mode = MutableStateFlow<Mode>(Mode.Main)

    val mode: StateFlow<Mode> = _mode

    private val _idToken = MutableStateFlow<String?>(null)
    val idToken: StateFlow<String?> = _idToken

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    val balance: StateFlow<String> = ws.balance
    val status: StateFlow<WebSocketStatus> = ws.status

    init {
        // Preload saved token if available
        prefs.getIdToken()?.let { _idToken.value = it }
    }
    private val _joinedTournamentIds = MutableStateFlow<Set<UUID>>(emptySet())
    val joinedTournamentIds: StateFlow<Set<UUID>> = _joinedTournamentIds.asStateFlow()

    fun setJoinedTournaments(ids: Set<UUID>) {
        _joinedTournamentIds.value = ids
    }

    suspend fun refreshToken(): String? {
        val user = FirebaseAuth.getInstance().currentUser ?: return null
        val freshToken = user.getIdToken(true).await().token
        _idToken.value = freshToken
        prefs.saveIdToken(freshToken!!)
        return freshToken
    }

    // Check if token is present and not expired
    suspend fun getValidToken(): String? {
        val token = _idToken.value
        return if (token == null || isTokenExpired(token)) {
            Log.d(TAG, "🔁 Token is missing or expired, refreshing...")
            refreshToken()
        } else {
            token
        }
    }

    // Parses JWT and checks `exp` field
    private fun isTokenExpired(token: String): Boolean {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return true

            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP))
            val json = JSONObject(payload)
            val exp = json.optLong("exp", 0)
            val now = System.currentTimeMillis() / 1000

            exp < now + 60 // expire early by 60s margin
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to parse token: ${e.message}")
            true
        }
    }

    fun connectIfNeeded() {
        scope.launch {
            if (_isConnected.value) return@launch
            val token = getValidToken() ?: return@launch
            ws.connect(token, _mode.value)
            _isConnected.value = true
        }
    }

    fun changeMode(mode: Mode) {
        println("🌐 [SessionManager] Received changeMode: $mode")
        _mode.value = mode
        ws.changeMode(mode)
    }

    fun disconnect() {
        ws.disconnect()
        _isConnected.value = false
    }

    fun logout() {
        disconnect()
        prefs.clear()
        FirebaseAuth.getInstance().signOut()
        _idToken.value = null
    }
}
