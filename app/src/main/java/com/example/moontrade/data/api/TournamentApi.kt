package com.example.moontrade.data.api

import com.example.moontrade.data.dto.TournamentDto
import com.example.moontrade.data.request.JoinTournamentRequest
import com.example.moontrade.data.response.JoinTournamentResponse
import retrofit2.http.*

import java.util.UUID

interface TournamentApi {

    // Fetch all tournaments with isJoined flag
    @GET("tournaments")
    suspend fun getTournaments(
        @Header("Authorization") bearerToken: String
    ): List<TournamentDto>

    // Join a specific tournament
    @POST("tournaments/{id}/join")
    suspend fun joinTournament(
        @Path("id") tournamentId: UUID,
        @Body request: JoinTournamentRequest
    ): JoinTournamentResponse
}
