package com.example.moontrade.data.api

import com.example.moontrade.data.response.OrdersResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface FetchOrdersApi {

    @GET("/orders")
    suspend fun getUserOrders(
        @Header("Authorization") token: String,
        @Query("mode") mode: String,
        @Query("tournament_id") tournamentId: String? = null
    ): OrdersResponse

    @DELETE("/orders/{id}")
    suspend fun cancelOrder(
        @Header("Authorization") token: String,
        @Path("id") orderId: String
    )
}