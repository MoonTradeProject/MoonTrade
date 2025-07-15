package com.example.moontrade.model

data class ProfileUpdateRequest(
    val username: String,
    val avatar_url: String,
    val description: String?,
    val tags: List<String>
)