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
import kotlinx.coroutines.flow.collectLatest
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

    // Derived "connected" flag from WebSocket status
    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    // Expose balance / roi / status directly from WebSocketManager
    val balance: StateFlow<String> = ws.balance
    val status: StateFlow<WebSocketStatus> = ws.status
    val roi: StateFlow<String> = ws.roi

    private val _joinedTournamentIds = MutableStateFlow<Set<UUID>>(emptySet())
    val joinedTournamentIds: StateFlow<Set<UUID>> = _joinedTournamentIds.asStateFlow()

    init {
        // Preload saved token if available
        prefs.getIdToken()?.let { _idToken.value = it }

        // Track WebSocketStatus and update _isConnected
        scope.launch {
            ws.status.collectLatest { st ->
                _isConnected.value = st is WebSocketStatus.Connected
                Log.d(TAG, "WS status changed: $st, isConnected=${_isConnected.value}")
            }
        }
    }

    fun setJoinedTournaments(ids: Set<UUID>) {
        _joinedTournamentIds.value = ids
    }

    // -----------------------------
    // TOKEN MANAGEMENT
    // -----------------------------

    suspend fun refreshToken(): String? {
        val user = FirebaseAuth.getInstance().currentUser ?: return null

        return try {
            val result = user.getIdToken(true).await()
            val freshToken = result.token

            if (freshToken != null) {
                _idToken.value = freshToken
                prefs.saveIdToken(freshToken)
                Log.d(TAG, "🔑 Token refreshed successfully")
            }

            freshToken
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to refresh token: ${e.message}")
            null
        }
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

            val payload = String(
                Base64.decode(
                    parts[1],
                    Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
                )
            )
            val json = JSONObject(payload)
            val exp = json.optLong("exp", 0)
            val now = System.currentTimeMillis() / 1000

            exp < now + 60 // expire early by 60s margin
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to parse token: ${e.message}")
            true
        }
    }

    // -----------------------------
    // CONNECTION MANAGEMENT
    // -----------------------------


    fun connectIfNeeded() {
        scope.launch {
            val token = getValidToken() ?: return@launch
            val st = ws.status.value


            if (st is WebSocketStatus.Connected ||
                st is WebSocketStatus.Connecting
            ) {
                Log.d(TAG, "connectIfNeeded(): already ok, skipping")
                return@launch
            }


            Log.d(TAG, "connectIfNeeded(): opening WS…")
            ws.connect(token, _mode.value)
        }
    }

    /**
     * Explicit hook you can call when app goes to foreground.
     * For example from Activity.onResume().
     */
    fun onAppForeground() {
        scope.launch {
            // 1. Refresh token always on foreground
            val token = getValidToken()

            if (token != null) {
                Log.d(TAG, "📱 Foreground: token OK")
            } else {
                Log.e(TAG, "📱 Foreground: FAILED TO REFRESH TOKEN")
            }

            // 2. Ensure WS connection
            connectIfNeeded()
        }
    }

    /**
     * Change trading mode (Main or Tournament).
     * If WS is already connected, just send changeMode.
     * If not connected, connect with new mode.
     */
    fun changeMode(mode: Mode) {
        Log.d(TAG, "🌐 [SessionManager] changeMode: $mode")
        _mode.value = mode

        scope.launch {
            val token = getValidToken() ?: return@launch

            when (ws.status.value) {
                is WebSocketStatus.Connected -> {
                    ws.sendChangeMode(mode)
                }
                else -> {
                    Log.d(TAG, "changeMode(): WS dead → reconnect")
                    ws.connect(token, mode)
                }
            }
        }
    }


    /**
     * User / app requested an explicit disconnect.
     */
    fun disconnect() {
        Log.d(TAG, "🔌 SessionManager.disconnect()")
        ws.disconnect()
    }

    fun logout() {
        Log.d(TAG, "🚪 SessionManager.logout()")
        disconnect()
        prefs.clear()
        FirebaseAuth.getInstance().signOut()
        _idToken.value = null
        _joinedTournamentIds.value = emptySet()
    }
}
