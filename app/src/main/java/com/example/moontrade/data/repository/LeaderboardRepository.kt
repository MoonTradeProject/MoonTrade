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
    suspend fun fetchLeaderboard(mode: Mode): LeaderboardResponse = runCatching {
        val token = session.getValidToken() ?: error("No valid token")
        val queryMap = mode.toQueryMap()

        android.util.Log.d("LeaderboardRepo", "📌 Fetching leaderboard with query: $queryMap")
        android.util.Log.d("LeaderboardRepo", "🔑 Using token: ${token.take(20)}...")

        val response = api.getLeaderboard(queryMap, "Bearer $token")
        android.util.Log.d("LeaderboardRepo", "✅ Leaderboard fetched successfully: $response")

        response
    }.getOrElse { e ->
        android.util.Log.e("LeaderboardRepo", "❌ Leaderboard fetch failed", e)
        throw e
    }
}

