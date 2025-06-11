package com.example.moontrade.data.repository

import com.example.moontrade.auth.AuthPreferences
import com.example.moontrade.data.api.TournamentApi
import com.example.moontrade.data.dto.TournamentDto
import com.example.moontrade.data.enums.TournamentPaymentMethod
import com.example.moontrade.data.request.JoinTournamentRequest
import com.example.moontrade.data.response.JoinTournamentResponse
import javax.inject.Inject

// Repository that wraps Tournament API and manages token injection
class TournamentRepository @Inject constructor(
    private val api: TournamentApi,
    private val authPreferences: AuthPreferences
) {
    suspend fun getTournaments(): List<TournamentDto> {
        val token = authPreferences.getIdToken()
            ?: throw IllegalStateException("User not logged in")
        return api.getTournaments("Bearer $token")
    }

    suspend fun joinTournament(tournamentId: String, method: TournamentPaymentMethod): JoinTournamentResponse {
        val token = authPreferences.getIdToken()
            ?: throw IllegalStateException("User not logged in")
        val request = JoinTournamentRequest(
            idToken = token,
            paymentMethod = method
        )
        return api.joinTournament(tournamentId, request)
    }
}
