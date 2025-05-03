package com.example.moontrade.ui.screens.authentication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moontrade.ui.screens.components.bars.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterCode(navController: NavController, authViewModel: Any) {
    var code by remember { mutableStateOf("") }

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
                .padding(24.dp)
        ) {
            OutlinedTextField(
                value = code,
                onValueChange = { code = it },
                label = { Text("6-digit code") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { navController.navigate("create_password") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Verify")
            }
        }
    }
}

