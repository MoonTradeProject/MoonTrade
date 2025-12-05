package com.example.moontrade.model


import java.math.BigDecimal

data class UserAsset(
    val asset_name: String,
    val amount: BigDecimal,
    val available: BigDecimal,
    val asset_value: BigDecimal,
)
