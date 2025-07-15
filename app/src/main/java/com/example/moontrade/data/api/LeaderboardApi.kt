package com.example.moontrade.data.api


import com.example.moontrade.model.LeaderboardEntry
import com.example.moontrade.model.LeaderboardResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface LeaderboardApi {
    @GET("/leaderboard")
    suspend fun getLeaderboard(
        @Query("mode") mode: String = "main",
        @Header("Authorization") auth: String
    ): LeaderboardResponse
}
