package com.example.moontrade.ui.theme


import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemePreferences @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
    private val KEY_DARK = "dark_mode"

    fun isDarkTheme(): Boolean = prefs.getBoolean(KEY_DARK, true)

    fun setDarkTheme(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_DARK, enabled).apply()
    }
}
