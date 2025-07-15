package com.example.moontrade.data.api

import com.example.moontrade.model.LeaderboardResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

import retrofit2.http.QueryMap

interface LeaderboardApi {
    @GET("/leaderboard")
    suspend fun getLeaderboard(
        @QueryMap query: Map<String, String>,
        @Header("Authorization") auth: String
    ): LeaderboardResponse
}

