package com.example.moontrade.data.repository

import com.example.moontrade.data.api.LeaderboardApi
import com.example.moontrade.model.LeaderboardResponse
import com.example.moontrade.session.SessionManager
import javax.inject.Inject

import com.example.moontrade.model.Mode

class LeaderboardRepository @Inject constructor(
    private val api: LeaderboardApi,
    private val session: SessionManager
) {
    suspend fun fetchLeaderboard(mode: Mode): LeaderboardResponse {
        val token = session.getValidToken() ?: error("No valid token")
        return api.getLeaderboard(mode.toQueryMap(), "Bearer $token")
    }
}

