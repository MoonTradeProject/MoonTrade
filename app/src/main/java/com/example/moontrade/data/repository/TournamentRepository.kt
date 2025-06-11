package com.example.moontrade.data.repository

import android.util.Log
import com.example.moontrade.data.api.TournamentApi
import com.example.moontrade.data.dto.TournamentDto
import com.example.moontrade.data.enums.TournamentPaymentMethod
import com.example.moontrade.data.request.JoinTournamentRequest
import com.example.moontrade.data.response.JoinTournamentResponse
import com.example.moontrade.session.SessionManager
import javax.inject.Inject

class TournamentRepository @Inject constructor(
    private val api: TournamentApi,
    private val session: SessionManager
) {
    suspend fun getTournaments(): List<TournamentDto> {
        val token = session.getValidToken()
        Log.d("TournamentRepo", "🔑 getTournaments() token = $token")
        if (token == null) throw Exception("Missing auth token")
        val result = api.getTournaments("Bearer $token")
        Log.d("TournamentRepo", "🪵 raw result: $result")
        return api.getTournaments("Bearer $token")

    }

    suspend fun joinTournament(tournamentId: String, method: TournamentPaymentMethod): JoinTournamentResponse {
        val token = session.getValidToken()
        Log.d("TournamentRepo", "🔑 joinTournament($tournamentId, $method) token = $token")
        if (token == null) throw Exception("Missing auth token")
        val req = JoinTournamentRequest(token, method)
        return api.joinTournament(tournamentId, req)
    }
}
