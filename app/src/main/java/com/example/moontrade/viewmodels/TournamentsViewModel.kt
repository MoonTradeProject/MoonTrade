package com.example.moontrade.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moontrade.data.dto.TournamentDto
import com.example.moontrade.data.enums.TournamentPaymentMethod
import com.example.moontrade.data.repository.TournamentRepository
import com.example.moontrade.data.response.JoinTournamentResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TournamentsViewModel @Inject constructor(
    private val tournamentRepository: TournamentRepository
) : ViewModel() {

    private val _tournaments = MutableStateFlow<List<TournamentDto>>(emptyList())
    val tournaments: StateFlow<List<TournamentDto>> = _tournaments.asStateFlow()

    private val _joinResult = MutableStateFlow<JoinTournamentResponse?>(null)
    val joinResult: StateFlow<JoinTournamentResponse?> = _joinResult.asStateFlow()

    fun loadTournaments() {
        viewModelScope.launch {
            try {
                _tournaments.value = tournamentRepository.getTournaments()
            } catch (e: Exception) {
                e.printStackTrace()
                _tournaments.value = emptyList()
            }
        }
    }

    fun joinTournament(tournamentId: String, method: TournamentPaymentMethod) {
        viewModelScope.launch {
            try {
                val response = tournamentRepository.joinTournament(tournamentId, method)
                _joinResult.value = response

                // Refresh the list to reflect isJoined change
                if (response.success) {
                    loadTournaments()
                }

            } catch (e: Exception) {
                _joinResult.value = JoinTournamentResponse(false, e.message ?: "Unknown error")
            }
        }
    }

    fun clearJoinResult() {
        _joinResult.value = null
    }
}
