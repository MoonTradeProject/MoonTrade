package com.example.moontrade.data.request

import com.example.moontrade.data.enums.TournamentPaymentMethod
import com.google.gson.annotations.SerializedName

// Payload sent when joining a tournament
data class JoinTournamentRequest(
    @SerializedName("id_token") val idToken: String,
    @SerializedName("payment_method") val paymentMethod: TournamentPaymentMethod
)