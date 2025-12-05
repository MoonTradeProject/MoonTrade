package com.example.moontrade.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.moontrade.data.ws.UnifiedWebSocketManager
import com.example.moontrade.model.OrderBookSnapshot
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class MarketDetailViewModel @Inject constructor(
    private val ws: UnifiedWebSocketManager
) : ViewModel() {

    val snapshot: StateFlow<OrderBookSnapshot?> = ws.orderBookSnapshot

    fun connect(symbol: String) {
        ws.connectOrderBook(symbol)
    }

    fun disconnect() {
        ws.disconnectOrderBook()
    }

    override fun onCleared() {
        disconnect()
        super.onCleared()
    }
}
