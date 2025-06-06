package com.example.moontrade.model

data class RegisterRequest(
    val id_token: String,
    val email: String,
    val username: String
)