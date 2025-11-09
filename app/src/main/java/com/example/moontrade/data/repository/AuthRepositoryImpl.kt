package com.example.moontrade.data.repository

import android.util.Log
import com.example.moontrade.BuildConfig
import com.example.moontrade.auth.AuthPreferences
import com.example.moontrade.auth.AuthRepository
import com.example.moontrade.auth.RegisterResult
import com.example.moontrade.data.api.AuthApi
import com.example.moontrade.model.RegisterRequest
import com.example.moontrade.session.SessionManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val prefs: AuthPreferences,
    private val session: SessionManager
) : AuthRepository {

    private val auth: FirebaseAuth = Firebase.auth
    private val _isAuth = MutableStateFlow(auth.currentUser != null)
    override fun getIsAuthenticatedFlow(): StateFlow<Boolean> = _isAuth.asStateFlow()

    override suspend fun register(email: String, password: String): RegisterResult = runCatching {
        Log.d("AuthRepositoryImpl", "📌 Starting Firebase registration for $email")

        auth.createUserWithEmailAndPassword(email, password).await()
        Log.d("AuthRepositoryImpl", "✅ Firebase account created")

        val token = auth.currentUser?.getIdToken(true)?.await()?.token
            ?: return RegisterResult.Error("Firebase token is null")

        prefs.saveIdToken(token)

        val req = RegisterRequest(token, email, email.substringBefore("@"))
        Log.d("AuthRepositoryImpl", "📤 Sending register request to backend: $req")
        Log.d("AuthRepositoryImpl", "🌍 Base URL: ${BuildConfig.BASE_URL}")
        try {
            authApi.register(req) // 🔥 это может упасть
            Log.d("AuthRepositoryImpl", "✅ Backend registration successful")
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "❌ Backend registration failed", e)

            try {
                val credential = EmailAuthProvider.getCredential(email, password)
                auth.currentUser?.reauthenticate(credential)?.await()
                auth.currentUser?.delete()?.await()
                Log.d("AuthRepositoryImpl", "✅ Firebase user deleted after failure")
            } catch (deleteError: Exception) {
                Log.e("AuthRepositoryImpl", "❌ Failed to delete Firebase user", deleteError)
            }

            return RegisterResult.Error("Server unavailable — registration cancelled")
        }

        session.connectIfNeeded()
        _isAuth.value = true
        RegisterResult.Success
    }.getOrElse { e ->
        Log.e("AuthRepositoryImpl", "❌ Full registration failed", e)
        RegisterResult.Error(e.message ?: "Unknown error")
    }

    override suspend fun login(email: String, password: String): Boolean = runCatching {
        auth.signInWithEmailAndPassword(email, password).await()

        val token = auth.currentUser?.getIdToken(true)?.await()?.token ?: return false
        prefs.saveIdToken(token)

        session.connectIfNeeded()
        _isAuth.value = true
        true
    }.getOrElse { e ->
        e.printStackTrace(); false
    }

    override suspend fun signInWithGoogle(account: GoogleSignInAccount): Boolean = runCatching {
        val cred = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(cred).await()

        val token = auth.currentUser?.getIdToken(true)?.await()?.token ?: return false
        prefs.saveIdToken(token)

        session.connectIfNeeded()
        _isAuth.value = true
        true
    }.getOrElse { e ->
        e.printStackTrace(); false
    }

    override fun logout() {
        session.logout()
        _isAuth.value = false
    }

    override fun isUserLoggedIn(): Boolean = auth.currentUser != null
}
