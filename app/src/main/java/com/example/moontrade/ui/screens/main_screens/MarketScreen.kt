package com.example.moontrade.ui.screens.main_screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moontrade.navigation.NavRoutes
import com.example.moontrade.ui.screens.components.bars.TopBar
import com.example.moontrade.viewmodels.MarketViewModel
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.random.Random

@Composable
fun MarketsScreen(
    navController: NavController,
    viewModel: MarketViewModel
) {
    val markets by viewModel.markets.collectAsState()
    var query by remember { mutableStateOf("") }

    val filteredMarkets = remember(markets, query) {
        markets.filter {
            it.contains(query.trim(), ignoreCase = true)
        }
    }

    Scaffold(
        topBar = {
            TopBar(title = "Markets", showBack = true, onBack = { navController.popBackStack() })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Search markets") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                singleLine = true
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Available Markets",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(filteredMarkets) { symbol ->
                    val base = symbol.take(3).uppercase()
                    val quote = symbol.drop(3).uppercase()
                    val pretty = "$base / $quote"
                    val fakePrice = remember(symbol) {
                        Random.nextDouble(0.001, 1.0)
                            .toBigDecimal()
                            .setScale(5, RoundingMode.HALF_UP)
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(NavRoutes.marketDetailRoute(symbol))
                            },
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = pretty, style = MaterialTheme.typography.bodyLarge)
                            Text(
                                text = fakePrice.toPlainString(),
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }
            }
        }
    }
}
