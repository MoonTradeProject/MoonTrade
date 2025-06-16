package com.example.moontrade.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themeSettings: ThemeSettings
) : ViewModel() {

    val isDarkTheme: StateFlow<Boolean> = themeSettings.isDarkTheme

    fun toggleTheme() {
        viewModelScope.launch {
            themeSettings.toggleTheme()
        }
    }
}
