package com.example.moontrade.model

data class PlaceOrderRequest(
    val mode: String,          // "main" / "tournament"
    val asset_name: String,
    val amount: String,        // строка для Decimal
    val price: String,         // строка для Decimal
    val exec_type: String,     // "market" / "limit"
    val order_type: String     // "buy" / "sell"
)