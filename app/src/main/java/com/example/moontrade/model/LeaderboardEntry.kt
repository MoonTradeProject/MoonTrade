package com.example.moontrade.model

data class LeaderboardEntry(
    val uid: String,
    val roi: Double,
    val rank: Int,
    val avatar_url: String? = null,
    val description: String? = null,
    val tags: List<String> = emptyList(),
    val achievements: List<String> = emptyList(),
    val username: String,
)