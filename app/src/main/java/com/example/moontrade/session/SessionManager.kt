package com.example.moontrade.session


import android.util.Log
import com.example.moontrade.auth.AuthPreferences
import com.example.moontrade.data.ws.WebSocketManager
import com.example.moontrade.model.Mode
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "SessionManager"

@Singleton
class SessionManager @Inject constructor(
    private val prefs: AuthPreferences,
    private val ws: WebSocketManager,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _mode = MutableStateFlow<Mode>(Mode.Main)
    val mode: StateFlow<Mode> = _mode

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    val balance: StateFlow<String> = ws.balance
    val status = ws.status

    fun connectIfNeeded() {
        scope.launch {
            if (_isConnected.value) return@launch

            val token = getFreshToken() ?: run {
                Log.w(TAG, "❌ No valid token for connection")
                return@launch
            }

            val mode = _mode.value
            prefs.saveIdToken(token)
            ws.connect(token, mode)
            _isConnected.value = true
        }
    }

    fun changeMode(mode: Mode) {
        _mode.value = mode
        ws.changeMode(mode)
    }

    fun disconnect() {
        ws.disconnect()
        _isConnected.value = false
    }

    private suspend fun getFreshToken(): String? {
        val user = FirebaseAuth.getInstance().currentUser ?: return null
        return user.getIdToken(false).await().token
    }

    fun logout() {
        disconnect()
        prefs.clear()
        FirebaseAuth.getInstance().signOut()
    }
}
