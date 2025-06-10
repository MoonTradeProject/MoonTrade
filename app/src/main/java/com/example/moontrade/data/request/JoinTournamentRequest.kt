package com.example.moontrade.data.request

import com.example.moontrade.data.enums.TournamentPaymentMethod

// Payload sent when joining a tournament
data class JoinTournamentRequest(
    val idToken: String,
    val paymentMethod: TournamentPaymentMethod
)
