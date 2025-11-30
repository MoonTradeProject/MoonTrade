package com.example.moontrade.ui.screens.authentication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moontrade.navigation.NavRoutes
import com.example.moontrade.ui.screens.components.buttons.PrimaryGradientButton
import com.example.moontrade.ui.theme.Violet600

@Composable
fun WelcomeScreen(navController: NavController) {
    val cs = MaterialTheme.colorScheme

    Scaffold(
        containerColor = cs.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "MoonTrade",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Violet600,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Welcome",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Learn, compete, and trade without risk.",
                    style = MaterialTheme.typography.titleMedium,
                    color = cs.onSurface,
                    textAlign = TextAlign.Center
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PrimaryGradientButton(
                    text = "Sign Up",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate(NavRoutes.ACCOUNT_CREATION)
                    }
                )
                Spacer(Modifier.height(12.dp))

                PrimaryGradientButton(
                    text = "Log In",
                    modifier = Modifier.fillMaxWidth(),
                    isSecondary = true,
                    onClick = {
                        navController.navigate(NavRoutes.LOGIN)
                    }
                )
            }
        }
    }
}
