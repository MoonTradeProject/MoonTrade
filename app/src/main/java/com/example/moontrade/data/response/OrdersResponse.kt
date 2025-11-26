package com.example.moontrade.data.response

data class OrderEntry(
    val id: String,
    val timestamp: String,
    val price: String,


    val amount: String,           // remaining_amount
    val original_amount: String,
    val executed_amount: String,

    val asset_name: String,
    val order_type: String,
    val exec_type: String,
    val mode: String,
    val tournament_id: String,
    val status: String
)


data class OrdersResponse(
    val orders: List<OrderEntry>,
    val mode: String,
    val tournament_id: String?
)
