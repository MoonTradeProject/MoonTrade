package com.example.moontrade.data.repository

import com.example.moontrade.data.api.MarketApi

import javax.inject.Inject

class MarketRepository @Inject constructor(
    private val api: MarketApi
) {
    suspend fun getMarkets(): Result<List<String>> {
        return try {
            val result = api.getMarkets()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}