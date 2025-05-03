package com.example.moontrade.ui.screens.main_screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.example.moontrade.ui.screens.components.bars.BottomBar
import com.example.moontrade.ui.screens.components.bars.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopBar(
                title = "Enter email",
                showBack = true,
                onBack = { navController.popBackStack() }
            )
        },
        bottomBar = {
            BottomBar(navController)
        }

    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Markets", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(8.dp))
            Text("Browse Forex, Crypto and Stock assets.")
        }
    }
}
