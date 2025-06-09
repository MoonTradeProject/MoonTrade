package com.example.moontrade.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moontrade.auth.AuthPreferences
import com.example.moontrade.data.ws.WebSocketManager
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
    // !!! forcing getting token/ maybe should not every time
    fun connect(mode: WebSocketManager.Mode) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val user = FirebaseAuth.getInstance().currentUser

            if (user == null) {
                println("[BalanceVM] ❌ No user logged in")
                ws.disconnect()
                return@launch
            }

            val tokenResult = user.getIdToken(true).await()
            val token = tokenResult.token

            if (token.isNullOrBlank()) {
                println("[BalanceVM] ❌ Token is null or blank")
                ws.disconnect()
                return@launch
            }

            println("[BalanceVM] ✅ Got fresh token")
            prefs.saveIdToken(token)
            ws.connect(token, mode)

        } catch (e: Exception) {
            println("[BalanceVM] ❌ Exception while getting token: ${e.message}")
            ws.disconnect()
        }
    }


    fun changeMode(mode: WebSocketManager.Mode) = ws.changeMode(mode)

    override fun onCleared() {
        ws.disconnect()
        super.onCleared()
    }
}
