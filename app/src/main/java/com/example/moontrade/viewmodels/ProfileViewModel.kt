package com.example.moontrade.viewmodels

import androidx.lifecycle.ViewModel
import com.example.moontrade.data.storage.ProfileStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val storage: ProfileStorage
) : ViewModel() {

    private val _nickname = MutableStateFlow(storage.loadNickname())
    val nickname: StateFlow<String> = _nickname

    private val _selectedTags = MutableStateFlow(storage.loadTags())
    val selectedTags: StateFlow<List<String>> = _selectedTags

    private val _avatarId = MutableStateFlow(storage.loadAvatarId())
    val avatarId: StateFlow<Int> = _avatarId

    val availableTags = listOf("Sniper", "Top 10", "Bullish", "Risky", "Calm")

    fun updateNickname(newName: String) {
        _nickname.value = newName
        storage.saveNickname(newName)
    }

    fun updateSelectedTags(newTags: List<String>) {
        _selectedTags.value = newTags
        storage.saveTags(newTags)
    }

    fun updateAvatarId(id: Int) {
        _avatarId.value = id
        storage.saveAvatarId(id)
    }
}
