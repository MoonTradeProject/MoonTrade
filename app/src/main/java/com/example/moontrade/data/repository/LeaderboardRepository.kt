package com.example.moontrade.data.repository

import com.example.moontrade.data.api.LeaderboardApi
import com.example.moontrade.model.LeaderboardResponse
import com.example.moontrade.session.SessionManager
import javax.inject.Inject

class LeaderboardRepository @Inject constructor(
    private val api: LeaderboardApi,
    private val session: SessionManager
) {
    suspend fun fetchLeaderboard(mode: String = "main"): LeaderboardResponse =
        session.getValidToken()?.let { token ->
            api.getLeaderboard(mode, "Bearer $token")
        } ?: error("No valid token")
}
