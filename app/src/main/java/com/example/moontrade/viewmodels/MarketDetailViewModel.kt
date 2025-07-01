package com.example.moontrade.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.moontrade.data.ws.OrderBookWebSocketManager
import com.example.moontrade.model.OrderBookSnapshot
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class MarketDetailViewModel @Inject constructor(
    private val orderBookWebSocketManager: OrderBookWebSocketManager
) : ViewModel() {

    val snapshot: StateFlow<OrderBookSnapshot?> = orderBookWebSocketManager.snapshot

    fun connect(symbol: String) {
        orderBookWebSocketManager.connect(symbol)
    }

    fun disconnect() {
        orderBookWebSocketManager.disconnect()
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}
