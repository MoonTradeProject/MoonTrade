package com.example.moontrade.model

data class LeaderboardResponse(
    val entries: List<LeaderboardEntry>,
    val user: LeaderboardEntry?,
    val mode: String,
    val tournament_id: String?
)
