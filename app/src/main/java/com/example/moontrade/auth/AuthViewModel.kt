package com.example.moontrade.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    fun isPasswordValid() = password.value.length >= 8
    fun isConfirmPasswordValid() = password.value == confirmPassword.value

    fun register(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            val success = authRepository.register(email.value, password.value)
            if (success) {
                _isLoggedIn.value = true
                onSuccess()
            } else {
                validationError.value = "Registration failed"
                onFailure("Registration failed")
            }
        }
    }

    fun login(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            val success = authRepository.login(email.value, password.value)
            if (success) {
                authPreferences.saveIsAuthenticated(true)
                _isLoggedIn.value = true
                onSuccess()
            } else {
                validationError.value = "Login failed"
                onFailure("Login failed")
            }
        }
    }

    fun signInWithGoogle(
        account: GoogleSignInAccount,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            val result = authRepository.signInWithGoogle(account)
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
    fun sendOtp(
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // TODO: Replace with Retrofit call to your backend that sends OTP to the email
                // Example:
                // val response = api.sendOtp(email.value)
                // if (response.success) { onSuccess() } else { onFailure("Error sending code") }

                println("[Mocked] Sending OTP to ${email.value}")
                onSuccess() // Simulate success
            } catch (e: Exception) {
                onFailure(e.message ?: "Unknown Error")
            }
        }
    }

    fun verifyOtp(
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                println(" [Mocked] Verifying OTP: email = ${email.value}, code = ${otpCode.value}")

                // TODO: Replace with Retrofit call to your backend that verifies OTP
                // Example:
                // val response = api.verifyOtp(email.value, otpCode.value)
                // if (response.success) { onSuccess() } else { onFailure("Wrong code") }

                val isValid = otpCode.value == "123456" // Temporary fake check
                if (isValid) {
                    onSuccess()
                } else {
                    onFailure("Wrong code")
                }
            } catch (e: Exception) {
                onFailure(e.message ?: "Error checking code")
            }
        }
    }

}
