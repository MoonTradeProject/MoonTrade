package com.example.moontrade.ui.screens.main_screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moontrade.navigation.NavRoutes
import com.example.moontrade.ui.screens.components.bars.TopBar
import com.example.moontrade.ui.theme.ThemeViewModel
import com.example.moontrade.viewmodels.MarketViewModel
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.random.Random

@Composable
fun MarketsScreen(
    navController: NavController,
    viewModel: MarketViewModel,
    themeViewModel: ThemeViewModel
) {
    val markets by viewModel.markets.collectAsState()
    var query by remember { mutableStateOf("") }

    val filteredMarkets = remember(markets, query) {
        markets.filter { it.contains(query.trim(), ignoreCase = true) }
    }
    val isDark  by themeViewModel.isDarkTheme.collectAsState()

    val cardColor = if (isDark) Color(0xFF1C1C1E) else Color(0xFFF4F4F4)
    val textColor = if (isDark) Color.White else Color.Black

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
                    .padding(bottom = 16.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.outline
                )
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = "Available Markets",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 4.dp),
                        color = textColor
                    )
                }

                items(filteredMarkets) { symbol ->
                    val base = symbol.take(3).uppercase()
                    val quote = symbol.drop(3).uppercase()
                    val pretty = "$base / $quote"

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(NavRoutes.marketDetailRoute(symbol))
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = cardColor),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 14.dp)
                        ) {
                            Text(
                                text = pretty,
                                style = MaterialTheme.typography.bodyLarge,
                                color = textColor
                            )
                        }
                    }
                }
            }
        }
    }
}

