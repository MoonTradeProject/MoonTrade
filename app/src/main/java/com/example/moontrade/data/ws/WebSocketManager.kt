package com.example.moontrade.data.ws

import com.example.moontrade.model.Mode
import com.example.moontrade.model.OrderBookSnapshot
import com.example.moontrade.model.WebSocketStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.StateFlow
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnifiedWebSocketManager @Inject constructor() {


    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // Клиент для баланса: с ping/pong
    private val balanceClient = OkHttpClient.Builder()
        .pingInterval(20, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .readTimeout(0, TimeUnit.MILLISECONDS)
        .build()

    // Клиент для стакана: без пинга (сервер сам пушит)
    private val orderBookClient = OkHttpClient.Builder()
        .pingInterval(0, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .readTimeout(0, TimeUnit.MILLISECONDS)
        .build()

    private val balanceWS = BalanceWebSocket(
        client = balanceClient,
        scope = scope
    )

    private val orderBookWS = OrderBookWebSocket(
        client = orderBookClient,
        scope = scope
    )

    // ---------- BALANCE API ----------

    fun connectBalance(token: String, mode: Mode) =
        balanceWS.connect(token, mode)

    fun disconnectBalance() =
        balanceWS.disconnect()

    fun changeMode(mode: Mode) =
        balanceWS.changeMode(mode)

    val balance = balanceWS.balance
    val roi = balanceWS.roi
    val balanceStatus: StateFlow<WebSocketStatus> = balanceWS.status

    // ---------- ORDER BOOK API ----------

    fun connectOrderBook(symbol: String) =
        orderBookWS.connect(symbol)

    fun disconnectOrderBook() =
        orderBookWS.disconnect()

    val orderBookSnapshot: StateFlow<OrderBookSnapshot?> = orderBookWS.snapshot
    val orderBookStatus: StateFlow<WebSocketStatus> = orderBookWS.status
}
