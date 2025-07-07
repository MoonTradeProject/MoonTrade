package com.example.moontrade.data.api

import com.example.moontrade.model.PlaceOrderRequest
import com.example.moontrade.model.PlaceOrderResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OrdersApi {

    @POST("orders")
    suspend fun placeOrder(
        @Body  request: PlaceOrderRequest,
        @Header("Authorization") bearerToken: String
    ): PlaceOrderResponse
}
