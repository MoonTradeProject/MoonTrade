package com.example.moontrade.model

data class OrderBookSnapshot(
    val bids: List<OrderBookLevel>,
    val asks: List<OrderBookLevel>
)
