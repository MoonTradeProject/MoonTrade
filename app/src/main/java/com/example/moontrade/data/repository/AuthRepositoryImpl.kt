package com.example.moontrade.data.repository

import com.example.moontrade.auth.AuthPreferences
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
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val prefs: AuthPreferences
) : AuthRepository {

    private val auth: FirebaseAuth = Firebase.auth
    private val _isAuth = MutableStateFlow(auth.currentUser != null)
    override fun getIsAuthenticatedFlow(): StateFlow<Boolean> = _isAuth.asStateFlow()

    /* ───────────────────────── register ───────────────────────── */

    override suspend fun register(email: String, password: String): Boolean = runCatching {
        println("[auth] register in Firebase…")
        auth.createUserWithEmailAndPassword(email, password).await()

        val token = auth.currentUser?.getIdToken(true)?.await()?.token ?: return false
        prefs.saveIdToken(token)
        println("[auth] Firebase token saved")

        val req = RegisterRequest(token, email, email.substringBefore("@"))
        authApi.register(req)
        println("[auth] backend /register OK")

        _isAuth.value = true
        true
    }.getOrElse { e ->
        e.printStackTrace(); false
    }

    /* ───────────────────────── login ───────────────────────── */

    override suspend fun login(email: String, password: String): Boolean = runCatching {
        auth.signInWithEmailAndPassword(email, password).await()

        val token = auth.currentUser?.getIdToken(true)?.await()?.token ?: return false
        prefs.saveIdToken(token)
        println("[auth] login token saved")

        _isAuth.value = true
        true
    }.getOrElse { e ->
        e.printStackTrace(); false
    }

    /* ───────────────────────── Google Sign-In ───────────────────────── */

    override suspend fun signInWithGoogle(account: GoogleSignInAccount): Boolean = runCatching {
        val cred = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(cred).await()

        val token = auth.currentUser?.getIdToken(true)?.await()?.token ?: return false
        prefs.saveIdToken(token)
        println("[auth] Google token saved")

        _isAuth.value = true
        true
    }.getOrElse { e ->
        e.printStackTrace(); false
    }

    /* ───────────────────────── logout ───────────────────────── */

    override fun logout() {
        auth.signOut()
        prefs.clear()
        _isAuth.value = false
        println("[auth] user logged out")
    }

    /* ───────────────────────── util ───────────────────────── */

    override fun isUserLoggedIn(): Boolean = auth.currentUser != null
}
