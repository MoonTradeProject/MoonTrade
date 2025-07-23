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
                .padding(24.dp)
        ) {
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    authViewModel.onPasswordChange(it)
                },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(), // ← СКРЫВАЕТ ТЕКСТ
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = confirm,
                onValueChange = {
                    confirm = it
                    authViewModel.onConfirmPasswordChange(it)
                },
                label = { Text("Confirm Password") },
                visualTransformation = PasswordVisualTransformation(), // ← И ТУТ ТОЖЕ
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    if (authViewModel.isPasswordValid() && authViewModel.isConfirmPasswordValid()) {
                        authViewModel.register(
                            onSuccess = {
                                navController.navigate("successful_registration")
                            },
                            onFailure = {

                            }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continue")
            }
        }
    }
}
