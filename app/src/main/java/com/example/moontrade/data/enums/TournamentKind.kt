package com.example.moontrade.data.enums

import com.google.gson.annotations.SerializedName

enum class TournamentKind {
    @SerializedName("daily")
    Daily,

    @SerializedName("weekly")
    Weekly,

    @SerializedName("two_weeks")
    TwoWeeks,

    @SerializedName("monthly")
    Monthly
}
