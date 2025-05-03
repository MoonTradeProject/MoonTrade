package com.example.moontrade.ui.screens.authentication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moontrade.ui.screens.components.bars.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountCreationScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopBar(
                title = "Enter Code",
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Create your account", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(32.dp))
            Button(onClick = {
                navController.navigate("add_mail")
            }) {
                Text("Continue")
            }
        }
    }
}
