package com.example.moontrade.data.repository

import android.util.Log
import com.example.moontrade.data.api.FetchOrdersApi
import com.example.moontrade.session.SessionManager
import com.example.moontrade.data.response.OrdersResponse
import com.example.moontrade.model.Mode
import com.google.gson.GsonBuilder
import javax.inject.Inject

// Pretty JSON helper
private val prettyGson = GsonBuilder().setPrettyPrinting().create()
private fun Any.prettyJson(): String = prettyGson.toJson(this)

class OrdersRepository @Inject constructor(
    private val api: FetchOrdersApi,
    private val sessionManager: SessionManager
) {

    suspend fun getOrders(): OrdersResponse {

        val rawToken = sessionManager.getValidToken()
            ?: throw IllegalStateException("Token missing")

        val token = "Bearer $rawToken"
        val mode = sessionManager.mode.value

        val response = when (mode) {
            Mode.Main -> api.getUserOrders(
                token = token,
                mode = "main",
                tournamentId = null
            )

            is Mode.Tournament -> api.getUserOrders(
                token = token,
                mode = "tournament",
                tournamentId = mode.tournamentId.toString()
            )
        }

        // 🔥 Pretty printed JSON
        Log.d("OrdersRepository", response.prettyJson())

        return response
    }

    suspend fun cancelOrder(id: String) {
        val rawToken = sessionManager.getValidToken()
            ?: throw IllegalStateException("Token missing")

        Log.d("OrdersRepository", "Cancel order id=$id")

        api.cancelOrder("Bearer $rawToken", id)
    }
}
