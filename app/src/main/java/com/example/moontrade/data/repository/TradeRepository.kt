package com.example.moontrade.data.repository

import com.example.moontrade.data.api.PlaceOrdersApi
import com.example.moontrade.model.PlaceOrderRequest
import com.example.moontrade.model.PlaceOrderResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TradeRepository @Inject constructor(
    private val ordersApi: PlaceOrdersApi
) {
    suspend fun placeOrder(
        req: PlaceOrderRequest,
        bearerToken: String
    ): PlaceOrderResponse = ordersApi.placeOrder(req, bearerToken)
}
