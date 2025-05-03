package com.example.moontrade.ui.screens.authentication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moontrade.ui.screens.components.bars.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmEmail(navController: NavController, authViewModel: Any) {
    Scaffold(
        topBar = {
        TopBar(
            title = "Confirm email",
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
            Text("We’ve sent you an email")
            Spacer(Modifier.height(16.dp))
            Text("Check your inbox and click the confirmation link.")
            Spacer(Modifier.height(32.dp))
            Button(
                onClick = { navController.navigate("enter_code") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("I’ve confirmed")
            }
        }
    }
}

