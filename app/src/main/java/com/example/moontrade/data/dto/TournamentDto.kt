package com.example.moontrade.data.dto

import com.example.moontrade.data.dto.enums.TournamentKind
import com.example.moontrade.data.enums.TournamentStatus
import java.time.LocalDateTime
import java.util.UUID

// This class represents a single tournament received from the backend
data class TournamentDto(
    val id: UUID,
    val name: String,
    val kind: TournamentKind,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val status: TournamentStatus,
    val isJoined: Boolean
)

