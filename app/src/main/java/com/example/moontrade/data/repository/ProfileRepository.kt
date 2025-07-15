package com.example.moontrade.data.repository


import android.util.Log
import com.example.moontrade.data.api.ProfileApi
import com.example.moontrade.model.ProfileUpdateRequest
import com.example.moontrade.session.SessionManager
import okhttp3.MultipartBody
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val api: ProfileApi,
    private val session: SessionManager
) {

    suspend fun uploadAvatar(filePart: MultipartBody.Part): String? {
        val token = session.getValidToken() ?: return null
        val response = api.uploadAvatar(filePart, "Bearer $token")

        if (!response.isSuccessful) {
            Log.e("AvatarUpload", "❌ Server error: ${response.code()} ${response.errorBody()?.string()}")
            return null
        }

        val body = response.body()
        if (body == null) {
            Log.e("AvatarUpload", "❌ Server responded with empty body")
            return null
        }

        Log.d("AvatarUpload", "✅ Server returned: $body")
        return body
    }


    suspend fun updateProfile(update: ProfileUpdateRequest): Boolean {
        val token = session.getValidToken() ?: return false
        val response = api.updateProfile(update, "Bearer $token")
        return response.isSuccessful
    }

}
