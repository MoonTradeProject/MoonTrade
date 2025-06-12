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

    val balance: StateFlow<String> = session.balance
    val status = session.status
    val mode: StateFlow<Mode> = session.mode
    val joinedTournamentIds: StateFlow<Set<UUID>> = session.joinedTournamentIds


    fun connect() = viewModelScope.launch {
        session.connectIfNeeded()
    }

    fun changeMode(mode: Mode) {
        println("🌐 [BalanceViewModel] changeMode: $mode")
        session.changeMode(mode)
    }

    override fun onCleared() {
        session.disconnect()
        super.onCleared()
    }
}
