package com.example.moontrade.ui.screens.authentication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SuccessfulRegistration(navController: NavController) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Registration Successful", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(16.dp))
            Text("You’re ready to start trading!")
            Spacer(Modifier.height(32.dp))
            Button(
                onClick = {
                    navController.navigate("home") {
                        popUpTo("onboarding1") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )  {
                Text("Start Now")
            }
        }
    }
}
