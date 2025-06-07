package com.example.moontrade.auth


import android.content.Context
import android.content.SharedPreferences

class AuthPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveIsAuthenticated(value: Boolean) {
        prefs.edit().putBoolean("is_authenticated", value).apply()
    }

    fun getIsAuthenticated(): Boolean {
        return prefs.getBoolean("is_authenticated", false)
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    fun saveIdToken(token: String) {
        prefs.edit().putString("id_token", token).apply()
    }

    fun getIdToken(): String? {
        return prefs.getString("id_token", null)
    }
}
