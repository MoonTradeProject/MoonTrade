package com.example.moontrade.ui.theme

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeSettings @Inject constructor(
    private val preferences: ThemePreferences
) {
    private val _isDarkTheme = MutableStateFlow(preferences.isDarkTheme())
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    fun toggleTheme() {
        val newValue = !_isDarkTheme.value
        _isDarkTheme.value = newValue
        preferences.setDarkTheme(newValue)
    }
}
