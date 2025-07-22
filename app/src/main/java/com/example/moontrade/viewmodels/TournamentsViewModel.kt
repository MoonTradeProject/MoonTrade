package com.example.moontrade.viewmodels


import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.moontrade.data.dto.TournamentDto
import com.example.moontrade.data.enums.TournamentPaymentMethod
import com.example.moontrade.data.enums.TournamentStatus
import com.example.moontrade.data.repository.TournamentRepository
import com.example.moontrade.data.response.JoinTournamentResponse
import com.example.moontrade.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TournamentsViewModel @Inject constructor(
    private val tournamentRepository: TournamentRepository,
    private val session: SessionManager
) : ViewModel() {

    private val _tournaments = MutableStateFlow<List<TournamentDto>>(emptyList())
    val tournaments: StateFlow<List<TournamentDto>> = _tournaments.asStateFlow()

    private val _joinResult = MutableStateFlow<JoinTournamentResponse?>(null)
    val joinResult: StateFlow<JoinTournamentResponse?> = _joinResult.asStateFlow()

    init {
        loadTournaments()
    }

    @OptIn(UnstableApi::class)
    internal fun loadTournaments() {
        viewModelScope.launch {
            try {
                Log.d("TournamentsVM", "📡 Loading tournaments...")
                val result = tournamentRepository.getTournaments()

                val activeTournaments = result.filter { it.status == TournamentStatus.Active }

                Log.d("TournamentsVM", "✅ Got ${activeTournaments.size} active tournaments")

                for (t in result) {
                    Log.d("TournamentsVM", "📋 ${t.name} | status=${t.status} | joined=${t.isJoined} | start=${t.startTime}")
                }

                val currentMode = session.mode.value
                if (currentMode is com.example.moontrade.model.Mode.Tournament) {
                    val stillExists = activeTournaments.any { it.id == UUID.fromString(currentMode.tournamentId) }

                    if (!stillExists) {
                        Log.w("TournamentsVM", "⛔ Tournament ${currentMode.tournamentId} is no longer active — switching to Main mode")
                        session.changeMode(com.example.moontrade.model.Mode.Main)
                    }
                }

                session.setJoinedTournaments(
                    activeTournaments.filter { it.isJoined }.map { it.id }.toSet()
                )

                _tournaments.value = activeTournaments

            } catch (e: Exception) {
                Log.e("TournamentsVM", "❌ Failed to load tournaments: ${e.message}", e)
                _tournaments.value = emptyList()
            }
        }
    }




    @OptIn(UnstableApi::class)

    fun joinTournament(tournamentId: String, method: TournamentPaymentMethod) {
        viewModelScope.launch {
            try {
                val token = session.getValidToken()
                Log.d("TournamentsVM", "🧾 Requesting join: tournamentId=$tournamentId, method=$method, token=${token?.take(10)}...")

                val response = tournamentRepository.joinTournament(tournamentId, method)

                Log.d("TournamentsVM", "✅ Server response: success=${response.success}, message='${response.message}'")
                _joinResult.value = response

                if (response.success) {
                    Log.d("TournamentsVM", "🔄 Refreshing tournament list after join...")
                    loadTournaments()
                } else {
                    Log.w("TournamentsVM", "⚠️ Join rejected: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("TournamentsVM", "❌ Join error: ${e.message}", e)
                _joinResult.value = JoinTournamentResponse(false, e.message ?: "Join failed")
            }
        }
    }



    fun clearJoinResult() {
        _joinResult.value = null
    }
}

