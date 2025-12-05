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

private const val TAG = "ProfileViewModel"

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val storage: ProfileStorage,
    private val repository: ProfileRepository
) : ViewModel() {

    /* local state backed by SharedPreferences -------------------------------- */

    private val _nickname     = MutableStateFlow(storage.loadNickname())
    val nickname: StateFlow<String> = _nickname

    private val _description = MutableStateFlow(storage.loadDescription())
    val description: StateFlow<String> = _description

    private val _selectedTags = MutableStateFlow(storage.loadTags())
    val selectedTags: StateFlow<List<String>> = _selectedTags

    private val _avatarId     = MutableStateFlow(storage.loadAvatarId()) // -1 = custom URL
    val avatarId: StateFlow<Int> = _avatarId

    private val _avatarUrl    = MutableStateFlow(storage.loadAvatarUrl()) // nullable
    val avatarUrl: StateFlow<String?> = _avatarUrl

    val availableTags = listOf("New Trader", "Flow Learner", "Trend Explorer", "Market Wanderer" )

    /** Base URL from build config (can be host:port or full http://host:port) */
    private val baseUrl = BuildConfig.BASE_URL

    /* Helpers ---------------------------------------------------------------- */

    // Build a full request from the *current* state, so we don't lose any fields.
    private fun buildRequest(): ProfileUpdateRequest = ProfileUpdateRequest(
        username    = nickname.value,
        avatar_url  = avatarUrl.value ?: "",
        description = description.value,      // keep current description
        tags        = selectedTags.value
    )

    // Centralized save: always PATCH a full profile
    fun saveProfile() {
        viewModelScope.launch {
            val req = ProfileUpdateRequest(
                username = nickname.value,
                avatar_url = avatarUrl.value ?: "",
                description = description.value,
                tags = selectedTags.value
            )
            val ok = repository.updateProfile(req)
            Log.d(TAG, if (ok) "✅ Profile saved" else "❌ Failed to save profile")
        }
    }

    /* setters ---------------------------------------------------------------- */

    /** Update nickname and immediately persist full profile */
    fun updateNickname(newName: String) {
        _nickname.value = newName
        storage.saveNickname(newName)
    }

    /** Update description and immediately persist full profile */

    fun updateDescription(newDesc: String) {
        _description.value = newDesc
        storage.saveDescription(newDesc)
    }

    /** Update tags and immediately persist full profile */
    fun updateSelectedTags(newTags: List<String>) {
        _selectedTags.value = newTags
        storage.saveTags(newTags)

    }

    /** User picked one of the built-in avatars -> set URL and persist */
    fun updateAvatarId(id: Int) {
        _avatarId.value = id
        storage.saveAvatarId(id)

        val extensions = mapOf(
            0 to "png", 1 to "png", 2 to "png", 3 to "png", 4 to "png",
            5 to "jpg", 6 to "jpg", 7 to "jpg", 8 to "jpg"
        )
        val ext = extensions[id] ?: "jpg"

        val path = "/avatars/avatar_${id}.$ext"
        val timestamp = System.currentTimeMillis()

        // If baseUrl already contains "http", use as-is; otherwise prefix http://
        val base = if (baseUrl.startsWith("http")) baseUrl else "http://$baseUrl"
        val fullUrl = "$base$path?t=$timestamp"

        _avatarUrl.value = fullUrl
        storage.saveAvatarUrl(fullUrl)

        // Built-in avatar means not custom upload
        _avatarId.value = id
        storage.saveAvatarId(id)

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
        repository.uploadAvatar(part)?.let { urlPathOrFull ->
            Log.d(TAG, "✅ Upload success: $urlPathOrFull")
            updateAvatarUrl(urlPathOrFull)
            onSuccess()
        } ?: Log.e(TAG, "❌ Upload failed")
    }

    /** Called after successful upload to the server: set URL and persist */
    private fun updateAvatarUrl(pathOrFull: String) {
        val timestamp = System.currentTimeMillis()

        // Accept both "/avatars/..." and "http://host:port/avatars/..."
        val full = if (pathOrFull.startsWith("http")) {
            pathOrFull
        } else {
            val base = if (baseUrl.startsWith("http")) baseUrl else "http://$baseUrl"
            "$base$pathOrFull"
        }
        val fullUrl = "$full?t=$timestamp"

        _avatarUrl.value = fullUrl
        storage.saveAvatarUrl(fullUrl)

        // Uploaded avatar is a custom URL -> set id = -1
        _avatarId.value = -1
        storage.saveAvatarId(-1)

        saveProfile()
    }
}

/* helper: compress Uri → JPEG byte[] ----------------------------------------- */
// Note: Keep it simple. We compress to JPEG to limit upload size.
//       If you need PNG for transparency, detect MIME and swap format.
private fun compressImage(context: Context, uri: Uri, quality: Int): ByteArray {
    val input  = context.contentResolver.openInputStream(uri)
        ?: throw IllegalArgumentException("Cannot open input stream for URI: $uri")
    val bitmap = BitmapFactory.decodeStream(input)
        ?: throw IllegalArgumentException("Cannot decode bitmap from URI: $uri")

    return ByteArrayOutputStream().apply {
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, this)
    }.toByteArray()
}
