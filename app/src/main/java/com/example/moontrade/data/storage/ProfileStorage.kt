package com.example.moontrade.data.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileStorage @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("profile", Context.MODE_PRIVATE)

    /* nickname ---------------------------------------------------------------- */

    fun saveNickname(nickname: String) =
        prefs.edit { putString("nickname", nickname) }

    fun loadNickname(): String =
        prefs.getString("nickname", "TraderX") ?: "TraderX"

    /* description ------------------------------------------------------------- */



    fun saveDescription(description: String) =
        prefs.edit { putString("description", description) }

    fun loadDescription(): String =
        prefs.getString("description", "") ?: ""

    /* tags -------------------------------------------------------------------- */

    fun saveTags(tags: List<String>) =
        prefs.edit { putString("tags", tags.joinToString(",")) }

    fun loadTags(): List<String> =
        prefs.getString("tags", "Sniper,Top 10")
            ?.split(',')
            ?.filter { it.isNotBlank() }
            ?: emptyList()

    /* built-in avatar id (-1 = custom url) ------------------------------------ */

    fun saveAvatarId(id: Int) =
        prefs.edit { putInt("avatarId", id) }

    fun loadAvatarId(): Int =
        prefs.getInt("avatarId", 0)

    /* custom avatar url ------------------------------------------------------- */

    fun saveAvatarUrl(url: String?) =
        prefs.edit {
            if (url != null) putString("avatarUrl", url) else remove("avatarUrl")
        }

    fun loadAvatarUrl(): String? =
        prefs.getString("avatarUrl", null)
}
