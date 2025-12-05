package com.example.moontrade.session

import android.util.Base64
import android.util.Log
import com.example.moontrade.auth.AuthPreferences
import com.example.moontrade.model.Mode
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import org.json.JSONObject
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "SessionManager"

@Singleton
class SessionManager @Inject constructor(
    private val prefs: AuthPreferences,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _mode = MutableStateFlow<Mode>(Mode.Main)
    // ---------------- MODE ----------------

    val mode: StateFlow<Mode> = _mode.asStateFlow()

    // ---------------- TOKEN ----------------
    private val _idToken = MutableStateFlow<String?>(prefs.getIdToken())
    val idToken: StateFlow<String?> = _idToken.asStateFlow()

    // ---------------- TOURNAMENTS ----------------
    private val _joinedTournamentIds = MutableStateFlow<Set<UUID>>(emptySet())
    val joinedTournamentIds: StateFlow<Set<UUID>> = _joinedTournamentIds.asStateFlow()

    init {
        // If token was cached → validate it
        scope.launch {
            val token = _idToken.value ?: return@launch
            if (isTokenExpired(token)) refreshToken()
        }
    }

    fun setJoinedTournaments(ids: Set<UUID>) {
        _joinedTournamentIds.value = ids
    }

    // ===============================================================
    // TOKEN MANAGEMENT (no WebSocket here)
    // ===============================================================

    suspend fun refreshToken(): String? {
        val user = FirebaseAuth.getInstance().currentUser ?: return null

        return try {
            val result = user.getIdToken(true).await()
            val fresh = result.token
            if (fresh != null) {
                _idToken.value = fresh
                prefs.saveIdToken(fresh)
                Log.d(TAG, "🔑 Token refreshed")
            }
            fresh
        } catch (e: Exception) {
            Log.e(TAG, "❌ Token refresh failed: ${e.message}")
            null
        }
    }

    suspend fun getValidToken(): String? {
        val token = _idToken.value
        return if (token == null || isTokenExpired(token)) {
            refreshToken()
        } else token
    }

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

            exp < now + 60 // expire 60s early
        } catch (e: Exception) {
            Log.e(TAG, "JWT parse error: ${e.message}")
            true
        }
    }

    // ===============================================================
    // MODE (no WebSocket calls)
    // ===============================================================

    fun changeMode(newMode: Mode) {
        Log.d(TAG, "🌐 changeMode → $newMode")

        val mode: StateFlow<Mode> = _mode.asStateFlow()
    }

    // ===============================================================
    // AUTH
    // ===============================================================
    fun onAppForeground() {

        scope.launch {
            getValidToken()
        }
    }
    fun logout() {
        Log.d(TAG, "🚪 SessionManager.logout()")

        prefs.clear()
        FirebaseAuth.getInstance().signOut()

        _idToken.value = null
        _mode.value = Mode.Main
        _joinedTournamentIds.value = emptySet()
    }
}
