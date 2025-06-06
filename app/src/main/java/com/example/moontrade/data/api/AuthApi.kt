package com.example.moontrade.data.api

import com.example.moontrade.model.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/register")
    suspend fun register(@Body request: RegisterRequest)
}
