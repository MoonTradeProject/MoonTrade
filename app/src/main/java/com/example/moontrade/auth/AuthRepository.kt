package com.example.moontrade.auth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    suspend fun register(email: String, password: String): Boolean
    suspend fun login(email: String, password: String): Boolean
    fun logout()
    fun getIsAuthenticatedFlow(): StateFlow<Boolean>
    suspend fun signInWithGoogle(account: GoogleSignInAccount): Boolean
    fun isUserLoggedIn(): Boolean
}


