package com.example.moontrade.ui.screens.main_screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moontrade.ui.screens.components.bars.BottomBar
import com.example.moontrade.ui.screens.components.bars.TopBar
import com.example.moontrade.viewmodels.MarketViewModel

@Composable
fun MarketsScreen(
    navController: NavController,
    viewModel: MarketViewModel
) {
    val markets by viewModel.markets.collectAsState()

    Scaffold(
        topBar = {
            TopBar(title = "Markets", showBack = true, onBack = { navController.popBackStack() })
        },
        bottomBar = { BottomBar(navController) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Available Markets",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(markets) { symbol ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            println("[markets] clicked $symbol")

                        },
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(text = symbol, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}
