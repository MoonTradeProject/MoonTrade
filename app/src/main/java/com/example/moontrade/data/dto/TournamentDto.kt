package com.example.moontrade.data.dto

import com.example.moontrade.data.enums.TournamentKind
import com.example.moontrade.data.enums.TournamentStatus
import java.time.LocalDateTime
import java.util.UUID

import com.google.gson.annotations.SerializedName


data class TournamentDto(
    @SerializedName("id") val id: UUID,
    @SerializedName("name") val name: String,
    @SerializedName("kind") val kind: TournamentKind,
    @SerializedName("start_time") val startTime: LocalDateTime,
    @SerializedName("end_time") val endTime: LocalDateTime,
    @SerializedName("status") val status: TournamentStatus,
    @SerializedName("is_joined") val isJoined: Boolean
)

