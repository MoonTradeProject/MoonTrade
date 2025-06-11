package com.example.moontrade.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moontrade.auth.AuthPreferences
import com.example.moontrade.data.ws.WebSocketManager
import com.example.moontrade.model.Mode
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class BalanceViewModel @Inject constructor(
    private val prefs: AuthPreferences,
    private val ws: WebSocketManager
) : ViewModel() {

    val balance: StateFlow<String> = ws.balance

    fun connect(mode: Mode) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                println("[BalanceVM] ❌ No user")
                ws.disconnect()
                return@launch
            }

            val token = user.getIdToken(false).await().token
            if (token.isNullOrBlank()) {
                println("[BalanceVM] ❌ Token is blank")
                ws.disconnect()
                return@launch
            }

            prefs.saveIdToken(token)
            ws.connect(token, mode)

        } catch (e: Exception) {
            println("[BalanceVM] ❌ Exception: ${e.message}")
            ws.disconnect()
        }
    }

    fun changeMode(mode: Mode) = ws.changeMode(mode)

    override fun onCleared() {
        ws.disconnect()
        super.onCleared()
    }
}
