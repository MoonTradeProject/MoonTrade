package com.example.moontrade.viewmodels


import androidx.lifecycle.ViewModel
import com.example.moontrade.model.LeaderboardEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SelectedPlayerViewModel @Inject constructor() : ViewModel() {
    private val _selected = MutableStateFlow<LeaderboardEntry?>(null)
    val selected: StateFlow<LeaderboardEntry?> = _selected.asStateFlow()

    fun set(player: LeaderboardEntry) {
        _selected.value = player
    }
}
