package com.example.moontrade.data.enums

import com.google.gson.annotations.SerializedName

enum class TournamentStatus {
    @SerializedName("active")
    Active,

    @SerializedName("finalizing")
    Finalizing,

    @SerializedName("finished")
    Finished
}
