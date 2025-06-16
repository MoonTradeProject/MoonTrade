package com.example.moontrade.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    private val _nickname = MutableStateFlow("TraderX")
    val nickname: StateFlow<String> = _nickname

    private val _selectedTags = MutableStateFlow(listOf("Sniper", "Top 10"))
    val selectedTags: StateFlow<List<String>> = _selectedTags

    val availableTags = listOf("Sniper", "Top 10", "Bullish", "Risky", "Calm")

    fun updateNickname(newName: String) {
        _nickname.value = newName
    }

    fun updateSelectedTags(newTags: List<String>) {
        _selectedTags.value = newTags
    }
}
