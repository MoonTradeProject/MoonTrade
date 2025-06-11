package com.example.moontrade.viewmodels


import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
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

    init {
        loadTournaments()
    }

    @OptIn(UnstableApi::class)
    private fun loadTournaments() {
        viewModelScope.launch {
            try {
                Log.d("TournamentsVM", "📡 Loading tournaments...")
                val result = tournamentRepository.getTournaments()
                Log.d("TournamentsVM", "✅ Got ${result.size} tournaments")
                _tournaments.value = result
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
                Log.d("TournamentsVM", "🧾 Joining tournament $tournamentId with method $method")
                val response = tournamentRepository.joinTournament(tournamentId, method)
                Log.d("TournamentsVM", "✅ Join result: ${response.success} — ${response.message}")
                _joinResult.value = response
                if (response.success) loadTournaments()
            } catch (e: Exception) {
                Log.e("TournamentsVM", "❌ Join failed: ${e.message}", e)
                _joinResult.value = JoinTournamentResponse(false, e.message ?: "Join failed")
            }
        }
    }


    fun clearJoinResult() {
        _joinResult.value = null
    }
}

