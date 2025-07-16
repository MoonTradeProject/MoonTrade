package com.example.moontrade.data.repository

import com.example.moontrade.data.api.UserApi
import com.example.moontrade.model.Mode
import com.example.moontrade.model.UserAsset
import com.example.moontrade.session.SessionManager
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val api: UserApi,
    private val session: SessionManager
) {

    suspend fun fetchUserAssets(mode: Mode): List<UserAsset> {
        val token = session.getValidToken() ?: error("No valid token")

        return when (mode) {
            is Mode.Main -> api.getUserAssets("main", null, "Bearer $token")
            is Mode.Tournament -> {
                val tid = mode.tournamentId
                println("📤 Sending Tournament ID = $tid")
                api.getUserAssets("tournament", tid.toString(), "Bearer $token")
            }
        }
    }
}
