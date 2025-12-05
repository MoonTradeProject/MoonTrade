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

//    suspend fun getOrders(): OrdersResponse {
//
//        val rawToken = sessionManager.getValidToken()
//            ?: throw IllegalStateException("Token missing")
//
//        val token = "Bearer $rawToken"
//        val mode = sessionManager.mode.value
//
//        val response = when (mode) {
//            Mode.Main -> api.getUserOrders(
//                token = token,
//                mode = "main",
//                tournamentId = null
//            )
//
//            is Mode.Tournament -> api.getUserOrders(
//                token = token,
//                mode = "tournament",
//                tournamentId = mode.tournamentId.toString()
//            )
//        }
//
//        // 🔥 Pretty printed JSON
//        Log.d("OrdersRepository", response.prettyJson())
//        Log.d("ORDERS", "TOKEN = $rawToken")
//        Log.d("ORDERS", "MODE = $mode")
//        return response
//    }
suspend fun getOrders(): OrdersResponse {

    val rawToken = sessionManager.getValidToken()
        ?: throw IllegalStateException("Token missing")

    val token = "Bearer $rawToken"
    val mode = sessionManager.mode.value

    // ---------- BEFORE REQUEST: FULL DIAGNOSTIC LOG ----------
    Log.d("ORDERS_REQ", "📤 Sending GET /orders request")
    Log.d("ORDERS_REQ", "  TOKEN: ${rawToken.take(12)}... (hidden)")
    Log.d("ORDERS_REQ", "  Mode object: $mode")

    when (mode) {
        Mode.Main -> {
            Log.d("ORDERS_REQ", "  → mode = \"main\"")
            Log.d("ORDERS_REQ", "  → tournamentId = null")
        }

        is Mode.Tournament -> {
            Log.d("ORDERS_REQ", "  → mode = \"tournament\"")
            Log.d("ORDERS_REQ", "  → tournamentId = ${mode.tournamentId}")
        }
    }

    // ---------- ACTUAL REQUEST ----------
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

    val json = response.prettyJson()
    val sizeBytes = json.toByteArray().size
    val sizeKb = sizeBytes / 1024.0

    Log.d("ORDERS_RES", "📥 Received response from /orders:")
    Log.d("ORDERS_RES", "  📦 Size: $sizeBytes bytes (${String.format("%.2f", sizeKb)} KB)")
    Log.d("ORDERS_RES", json)

    return response
}


    suspend fun cancelOrder(id: String) {
        val rawToken = sessionManager.getValidToken()
            ?: throw IllegalStateException("Token missing")

        Log.d("OrdersRepository", "Cancel order id=$id")

        api.cancelOrder("Bearer $rawToken", id)
    }
}
