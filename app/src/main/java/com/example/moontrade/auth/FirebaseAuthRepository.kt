package com.example.moontrade.data

import com.example.moontrade.auth.AuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    private val _isAuthenticated = MutableStateFlow(auth.currentUser != null)
    override fun getIsAuthenticatedFlow(): StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    override suspend fun register(email: String, password: String): Boolean {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            _isAuthenticated.value = true
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun login(email: String, password: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            _isAuthenticated.value = true
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun logout() {
        auth.signOut()
        _isAuthenticated.value = false
    }

    override suspend fun signInWithGoogle(account: GoogleSignInAccount): Boolean {
        return try {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential).await()
            _isAuthenticated.value = true
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
