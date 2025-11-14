package com.example.moontrade.model

data class OrderBookSnapshot(
    val bids: List<OrderBookLevel>,
    val asks: List<OrderBookLevel>,
    val matches: List<TradeMatch>? = emptyList()
)
// This we need to show the matched order list under the order book
data class TradeMatch(
    val price: Double,
    val qty: Double,
    val side: String,
    val timestamp: Long
)