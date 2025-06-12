package com.example.moontrade.data.enums

import com.google.gson.annotations.SerializedName

// User's choice of how to enter the tournament
enum class TournamentPaymentMethod {
    @SerializedName("main_balance")
    MainBalance,

    @SerializedName("ad")
    Ad,

    @SerializedName("purchase")
    Purchase
}
