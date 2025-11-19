package com.example.moontrade.utils

import com.example.moontrade.model.OrderBookLevel
import com.example.moontrade.model.OrderBookSnapshot

fun PriceCounter(
    snapshot: OrderBookSnapshot?,
    amount: String?,
    side: String
): Any {
    val amountDouble = amount?.toDoubleOrNull() ?: return 0.0
    if (amountDouble <= 0) return 0.0
    if (snapshot == null) return 0.0

    val levels: List<OrderBookLevel> = when (side.lowercase()) {
        "buy" -> snapshot.asks ?: emptyList()
        "sell" -> snapshot.bids ?: emptyList()
        else -> return 0.0
    }

    if (levels.isEmpty()) return Double.NaN

    var remaining = amountDouble
    var totalCost = 0.0

    levels.forEach { level ->
        if (remaining <= 0) return@forEach

        val price = level.price
        val vol = level.volume
        if (price <= 0 || vol <= 0) return@forEach

        val taken = minOf(remaining, vol)
        totalCost += taken * price
        remaining -= taken
    }

    return if (remaining > 0) "Not enough liquidity" else "=$$totalCost"
}