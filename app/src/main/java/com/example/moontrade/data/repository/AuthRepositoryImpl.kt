package com.example.moontrade.data.repository

import com.example.moontrade.auth.AuthRepository
import com.example.moontrade.data.api.AuthApi
import com.example.moontrade.model.RegisterRequest
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val authApi: AuthApi
) : AuthRepository {

    private val auth: FirebaseAuth = Firebase.auth
    private val _isAuthenticated = MutableStateFlow(auth.currentUser != null)
    override fun getIsAuthenticatedFlow(): StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    override suspend fun register(email: String, password: String): Boolean {
        return try {
            println("[auth] registering in Firebase...")
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: return false

            val token = user.getIdToken(false).await().token ?: return false
            println("[auth] Firebase ID token obtained")

            val req = RegisterRequest(
                id_token = token,
                email = email,
                username = email.substringBefore("@")
            )

            println("[auth] sending /register to backend")
            authApi.register(req)
            println("[auth] backend responded OK")

            _isAuthenticated.value = true
            true
        } catch (e: Exception) {
            e.printStackTrace()
            println("[auth] registration failed: ${e.message}")
            false
        }
    }

    override suspend fun login(email: String, password: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            _isAuthenticated.value = true
            println("[auth] login OK")
            true
        } catch (e: Exception) {
            e.printStackTrace()
            println("[auth] login failed: ${e.message}")
            false
        }
    }

    override fun logout() {
        auth.signOut()
        _isAuthenticated.value = false
        println("[auth] user logged out")
    }

    override suspend fun signInWithGoogle(account: GoogleSignInAccount): Boolean {
        return try {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential).await()
            _isAuthenticated.value = true
            println("[auth] google sign-in success")
            true
        } catch (e: Exception) {
            e.printStackTrace()
            println("[auth] google sign-in failed: ${e.message}")
            false
        }
    }
}
