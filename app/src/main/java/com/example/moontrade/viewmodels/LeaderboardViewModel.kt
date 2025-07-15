package com.example.moontrade.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moontrade.data.repository.LeaderboardRepository
import com.example.moontrade.model.LeaderboardEntry
import com.example.moontrade.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val repository: LeaderboardRepository,
    private val session: SessionManager
) : ViewModel() {

    private val _entries = MutableStateFlow<List<LeaderboardEntry>>(emptyList())
    val entries: StateFlow<List<LeaderboardEntry>> = _entries.asStateFlow()

    fun loadLeaderboard() {
        viewModelScope.launch {
            try {
                val mode = session.mode.value
                println("📥 [LeaderboardViewModel] Loading leaderboard with mode=$mode")

                val response = repository.fetchLeaderboard(mode)

                println("✅ [LeaderboardViewModel] Loaded ${response.entries.size} entries")
                response.entries.forEach {
                    println("➡ ${it.username}: ROI=${it.roi}, rank=${it.rank}")
                }

                _entries.value = response.entries
            } catch (e: Exception) {
                println("❌ [LeaderboardViewModel] Failed to load leaderboard: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}
