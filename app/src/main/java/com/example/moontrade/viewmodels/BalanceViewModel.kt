// File: com/example/moontrade/viewmodels/BalanceViewModel.kt
//
// ViewModel that exposes balance / ROI and lets the UI switch modes.
// Note: we no longer call `session.connectIfNeeded()` after every
// `changeMode` – SessionManager решает, нужно ли переподключаться.

package com.example.moontrade.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moontrade.model.Mode
import com.example.moontrade.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class BalanceViewModel @Inject constructor(
    private val session: SessionManager
) : ViewModel() {

    // Flows directly proxied from SessionManager / WebSocketManager
    val balance: StateFlow<String>           = session.balance
    val status                               = session.status
    val mode: StateFlow<Mode>                = session.mode
    val joinedTournamentIds: StateFlow<Set<UUID>> = session.joinedTournamentIds
    val roi: StateFlow<String>               = session.roi

    /** Kick-off initial connection from UI layer */
    fun connect() = viewModelScope.launch {
        session.connectIfNeeded()
    }

    /** Switch trading / UI mode (Main or Tournament) */
    fun changeMode(mode: Mode) {
        println("🌐 [BalanceViewModel] changeMode: $mode")
        session.changeMode(mode)          // one call is enough
        // session.connectIfNeeded()      // <- removed as redundant
    }

    override fun onCleared() {
        session.disconnect()
        super.onCleared()
    }
}
