package com.example.moontrade.data.api


import retrofit2.http.GET

interface MarketApi {
    @GET("/markets")
    suspend fun getMarkets(): List<String>
}
