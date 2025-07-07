package com.example.moontrade.model

import com.google.gson.JsonElement

data class PlaceOrderRequest(
    val mode: JsonElement, // <-- ключевой момент
    val asset_name: String,
    val amount: String,
    val price: String,
    val exec_type: String,
    val order_type: String
)