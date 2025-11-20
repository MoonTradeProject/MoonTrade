package com.example.moontrade.data.repository

import com.example.moontrade.data.api.FetchOrdersApi
import com.example.moontrade.session.SessionManager
import com.example.moontrade.data.response.OrdersResponse
import com.example.moontrade.model.Mode
import javax.inject.Inject

class OrdersRepository @Inject constructor(
    private val api: FetchOrdersApi,
    private val sessionManager: SessionManager
) {

    suspend fun getOrders(): OrdersResponse {

        val rawToken = sessionManager.getValidToken()
            ?: throw IllegalStateException("Token missing")

        val token = "Bearer $rawToken"
        val mode = sessionManager.mode.value

        return when (mode) {
            Mode.Main ->
                api.getUserOrders(token, "main")

            is Mode.Tournament ->
                api.getUserOrders(
                    token,
                    "tournament",
                    mode.tournamentId.toString()
                )
        }
    }

    suspend fun cancelOrder(id: String) {
        val rawToken = sessionManager.getValidToken()
            ?: throw IllegalStateException("Token missing")

        api.cancelOrder("Bearer $rawToken", id)
    }
}

