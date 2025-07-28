package com.example.moontrade.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moontrade.data.repository.ProfileRepository
import com.example.moontrade.data.storage.ProfileStorage
import com.example.moontrade.model.ProfileUpdateRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import com.example.moontrade.BuildConfig
private const val TAG = "AvatarUpload"

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val storage: ProfileStorage,
    private val repository: ProfileRepository
) : ViewModel() {

    /* local state backed by SharedPreferences -------------------------------- */

    private val _nickname      = MutableStateFlow(storage.loadNickname())
    val nickname: StateFlow<String> = _nickname

    private val _selectedTags  = MutableStateFlow(storage.loadTags())
    val selectedTags: StateFlow<List<String>> = _selectedTags

    private val _avatarId      = MutableStateFlow(storage.loadAvatarId()) // -1 = custom
    val avatarId: StateFlow<Int> = _avatarId

    private val _avatarUrl     = MutableStateFlow(storage.loadAvatarUrl()) // nullable
    val avatarUrl: StateFlow<String?> = _avatarUrl

    val availableTags = listOf("Sniper", "Top 10", "Bullish", "Risky", "Calm")

    /* setters ---------------------------------------------------------------- */

    fun updateNickname(newName: String) {
        _nickname.value = newName
        storage.saveNickname(newName)
    }

    fun updateSelectedTags(newTags: List<String>) {
        _selectedTags.value = newTags
        storage.saveTags(newTags)
    }

    /** User picked one of the built-in avatars. */
    fun updateAvatarId(id: Int, description: String = "") {
        _avatarId.value = id
        storage.saveAvatarId(id)


        val extensions = mapOf(
            0 to "png", 1 to "png", 2 to "png", 3 to "png", 4 to "png",
            5 to "jpg", 6 to "jpg", 7 to "jpg", 8 to "jpg"
        )

        val ext = extensions[id] ?: "jpg" // fallback
        val path = "/avatars/avatar_${id}.$ext"
        val timestamp = System.currentTimeMillis()
        val fullUrl = "$baseUrl$path?t=$timestamp"

        _avatarUrl.value = fullUrl
        storage.saveAvatarUrl(fullUrl)

        saveProfile(description)
    }

    /** Called after successful upload to the server. */
    val baseUrl = BuildConfig.BASE_URL

    private fun updateAvatarUrl(path: String) {
        val timestamp = System.currentTimeMillis()
        Log.d("ConfigCheck", "🔧 BuildConfig.BASE_URL = ${BuildConfig.BASE_URL}")

        val base = if (baseUrl.startsWith("http")) baseUrl else "http://$baseUrl"
        val fullUrl = "$base$path?t=$timestamp"

        Log.d("AvatarDebug", "✅ Fixed fullUrl: $fullUrl")

        _avatarUrl.value = fullUrl
        storage.saveAvatarUrl(fullUrl)

        _avatarId.value = -1
        storage.saveAvatarId(-1)
    }



    /* avatar upload ---------------------------------------------------------- */

    fun uploadAvatarFromUri(
        context: Context,
        uri: Uri,
        onSuccess: () -> Unit = {}
    ) = viewModelScope.launch {
        Log.d(TAG, "📤 Starting upload from URI: $uri")

        val mediaType = (context.contentResolver.getType(uri) ?: "image/jpeg")
            .toMediaTypeOrNull() ?: run {
            Log.e(TAG, "❌ Unsupported media type")
            return@launch
        }

        val bytes = compressImage(context, uri, 75)
        val body  = bytes.toRequestBody(mediaType)
        val part  = MultipartBody.Part.createFormData("file", "avatar.jpg", body)

        Log.d(TAG, "📤 Uploading ${bytes.size} bytes …")
        repository.uploadAvatar(part)?.let { url ->
            Log.d(TAG, "✅ Upload success: $url")
            updateAvatarUrl(url)
            onSuccess()
        } ?: Log.e(TAG, "❌ Upload failed")
    }

    /* save profile to backend ------------------------------------------------- */

    fun saveProfile(description: String) {
        viewModelScope.launch {
            val req = ProfileUpdateRequest(
                username    = nickname.value,
                avatar_url  = avatarUrl.value ?: "",
                description = description,
                tags        = selectedTags.value
            )
            val ok = repository.updateProfile(req)
            Log.d(TAG, if (ok) "✅ Profile saved" else "❌ Failed to save profile")
        }
    }
}

/* helper: compress Uri → JPEG byte[] ----------------------------------------- */

private fun compressImage(context: Context, uri: Uri, quality: Int): ByteArray {
    val input  = context.contentResolver.openInputStream(uri)
        ?: throw IllegalArgumentException("Cannot open input stream for URI: $uri")
    val bitmap = BitmapFactory.decodeStream(input)
        ?: throw IllegalArgumentException("Cannot decode bitmap from URI: $uri")

    return ByteArrayOutputStream().apply {
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, this)
    }.toByteArray()
}
