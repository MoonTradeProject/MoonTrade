// File: com/example/moontrade/viewmodels/BalanceViewModel.kt
//
// ViewModel that exposes balance / ROI and lets the UI switch modes.
// Note: we no longer call `session.connectIfNeeded()` after every
// `changeMode` – SessionManager решает, нужно ли переподключаться.

package com.example.moontrade.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moontrade.data.ws.UnifiedWebSocketManager
import com.example.moontrade.model.Mode
import com.example.moontrade.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class BalanceViewModel @Inject constructor(
    private val session: SessionManager,
    private val ws: UnifiedWebSocketManager
) : ViewModel() {

    // ----- FLOWS -----
    val balance = ws.balance
    val roi = ws.roi
    val status = ws.balanceStatus

    val mode = session.mode
    val joinedTournamentIds = session.joinedTournamentIds

    /** Start WebSocket connection */
    fun connect() = viewModelScope.launch {
        val token = session.getValidToken() ?: return@launch
        ws.connectBalance(token, session.mode.value)
    }

    /** Switch UI trading mode */
    fun changeMode(mode: Mode) {
        session.changeMode(mode)
        ws.changeMode(mode)
    }

    override fun onCleared() {
        ws.disconnectBalance()
        super.onCleared()
    }
}
