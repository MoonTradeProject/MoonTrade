package com.example.moontrade.data.api

import com.example.moontrade.model.UserAsset
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface AssetsApi {
    @GET("user/assets")
    suspend fun getUserAssets(
        @Query("mode") mode: String,
        @Query("tournament_id") tournamentId: String?,
        @Header("Authorization") bearer: String
    ): List<UserAsset>
}