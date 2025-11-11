package com.example.moontrade.data.api

import com.example.moontrade.model.UserAsset
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface AssetsApi {
    @GET("user/assets")
    suspend fun getUserAssets(
        @Query("scope") scope: String,                 // "main" | "tournament"
        @Query("tournament_id") tournamentId: String?, // null для main
        @Header("Authorization") bearer: String
    ): List<UserAsset>
}
