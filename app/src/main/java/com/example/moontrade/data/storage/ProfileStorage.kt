package com.example.moontrade.data.storage


import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class ProfileStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("profile", Context.MODE_PRIVATE)

    fun saveNickname(nickname: String) {
        prefs.edit() { putString("nickname", nickname) }
    }

    fun loadNickname(): String {
        return prefs.getString("nickname", "TraderX") ?: "TraderX"
    }

    fun saveTags(tags: List<String>) {
        prefs.edit() { putString("tags", tags.joinToString(",")) }
    }

    fun loadTags(): List<String> {
        val raw = prefs.getString("tags", "Sniper,Top 10") ?: ""
        return raw.split(",").filter { it.isNotBlank() }
    }
}
