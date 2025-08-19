package com.example.moontrade.ui.screens.authentication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moontrade.auth.AuthViewModel
import com.example.moontrade.ui.screens.components.bars.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePassword(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var registrationError by remember { mutableStateOf<String?>(null) }

    val isPasswordValid = authViewModel.isPasswordValid()
    val doPasswordsMatch = password == confirm
    val showErrors = remember(password, confirm) { password.isNotEmpty() || confirm.isNotEmpty() }

    Scaffold(
        topBar = {
            TopBar(
                title = "Create password",
                showBack = true,
                onBack = { navController.popBackStack() }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    authViewModel.onPasswordChange(it)
                    registrationError = null // сбрасываем ошибку при новом вводе
                },
                label = { Text("Password") },
                supportingText = { Text("At least 6 characters, including 1 uppercase letter and 1 number") },
                isError = showErrors && !isPasswordValid,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            if (showErrors && !isPasswordValid) {
                Text(
                    text = "Password must be at least 6 characters",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            OutlinedTextField(
                value = confirm,
                onValueChange = {
                    confirm = it
                    authViewModel.onConfirmPasswordChange(it)
                    registrationError = null // сбрасываем ошибку при новом вводе
                },
                label = { Text("Confirm Password") },
                isError = showErrors && !doPasswordsMatch,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            if (showErrors && !isPasswordValid) {
                Text(
                    text = "Password must be at least 6 characters,\ncontain an uppercase letter and a number",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // 🔥 Ошибка от Firebase (например, email уже используется)
            if (registrationError != null) {
                Text(
                    text = registrationError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    if (isPasswordValid && doPasswordsMatch) {
                        authViewModel.register(
                            onSuccess = {
                                navController.navigate("successful_registration")
                            },
                            onFailure = { error ->
                                registrationError = error
                            }

                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = password.isNotEmpty() && confirm.isNotEmpty()
            ) {
                Text("Continue")
            }
        }
    }
}
