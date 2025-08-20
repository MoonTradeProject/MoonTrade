package com.example.moontrade.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val authPreferences: AuthPreferences
) : ViewModel() {

    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var confirmPassword = mutableStateOf("")
    var otpCode = mutableStateOf("")
    var validationError = mutableStateOf<String?>(null)
    var touchIdEnabled = mutableStateOf(false)

    private val _isLoggedIn = MutableStateFlow(authRepository.getIsAuthenticatedFlow().value)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun onEmailChange(newEmail: String) {
        email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password.value = newPassword
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        confirmPassword.value = newConfirmPassword
    }

    fun onTouchIdChange(newValue: Boolean) {
        touchIdEnabled.value = newValue
    }

    fun isPasswordValid(): Boolean {
        val passwordValue = password.value
        return passwordValue.length >= 6 &&
                passwordValue.any { it.isUpperCase() } &&
                passwordValue.any { it.isDigit() }
    }

    fun register(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                when (val result = authRepository.register(email.value, password.value)) {
                    is RegisterResult.Success -> {
                        _isLoggedIn.value = true
                        onSuccess()
                    }
                    is RegisterResult.Error -> {
                        validationError.value = result.message
                        onFailure(result.message)
                    }

                }
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Server unavailable"
                validationError.value = errorMessage
                onFailure(errorMessage)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun login(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = authRepository.login(email.value, password.value)
                if (success) {
                    authPreferences.saveIsAuthenticated(true)
                    _isLoggedIn.value = true
                    onSuccess()
                } else {
                    validationError.value = "Invalid credentials"
                    onFailure("Invalid credentials")
                }
            } catch (e: Exception) {
                val msg = e.message ?: "Server unavailable"
                validationError.value = msg
                onFailure(msg)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun signInWithGoogle(
        account: GoogleSignInAccount,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = authRepository.signInWithGoogle(account)
            _isLoading.value = false
            if (result) {
                _isLoggedIn.value = true
                authPreferences.saveIsAuthenticated(true)
                onSuccess()
            } else {
                validationError.value = "Google Sign-In failed"
                onFailure("Google Sign-In failed")
            }
        }
    }

    fun logout() {
        authRepository.logout()
        authPreferences.clear()
        _isLoggedIn.value = false
    }

    fun resetFields() {
        email.value = ""
        password.value = ""
        confirmPassword.value = ""
        otpCode.value = ""
        validationError.value = null
    }
}
sealed class RegisterResult {
    object Success : RegisterResult()
    data class Error(val message: String) : RegisterResult()
}