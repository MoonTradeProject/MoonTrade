package com.example.moontrade.model

data class BalanceResponse(
    val balance: String,
    val roi: Double?,
    val timestamp: Long
)
