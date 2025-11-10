package com.example.moontrade.ui.screens.main_screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.moontrade.ui.screens.components.orderbook.OrderBookLive
import com.example.moontrade.viewmodels.MarketDetailViewModel
import com.example.moontrade.viewmodels.TradeViewModel

@Composable
fun MarketDetailScreen(
    navController: NavController,
    symbol: String,
    viewModel: MarketDetailViewModel
) {
    val tradeViewModel: TradeViewModel = hiltViewModel()

    /* ------------- LOCAL UI STATE ------------- */
    var orderType by remember { mutableStateOf("Limit") }
    var isBuy     by remember { mutableStateOf(true) }
//    var stopLossEnabled   by remember { mutableStateOf(false) }
//    var stopLoss          by remember { mutableStateOf("") }
//    var takeProfitEnabled by remember { mutableStateOf(false) }
//    var takeProfit        by remember { mutableStateOf("") }

    /* ------------- SNAPSHOT ------------- */
    val snapshot by viewModel.snapshot.collectAsState()

    LaunchedEffect(symbol) {
        viewModel.connect(symbol)
        tradeViewModel.assetName.value = symbol
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        /* Header */
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(symbol, style = MaterialTheme.typography.headlineSmall)
        }

        Spacer(Modifier.height(16.dp))

        Text(
            "Best Bid: ${snapshot?.bids?.firstOrNull()?.price ?: "-"}    |    " +
                    "Best Ask: ${snapshot?.asks?.firstOrNull()?.price ?: "-"}"
        )

        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth()) {
            OrderBookLive(snapshot = snapshot)
        }

        Spacer(Modifier.height(24.dp))

        /* Order type */
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Order Type:")
            Spacer(Modifier.width(8.dp))
            DropdownMenuBox(
                selected = orderType,
                options = listOf("Limit", "Market"),
                onSelected = { orderType = it }
            )
        }

        /* Side */
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Side:")
            Spacer(Modifier.width(8.dp))
            SegmentedButton(
                options = listOf("Buy", "Sell"),
                selectedIndex = if (isBuy) 0 else 1,
                onSelectedIndex = { isBuy = it == 0 }
            )
        }

        /* ---------- PRICE (only for Limit) ---------- */
        if (orderType == "Limit") {
            OutlinedTextField(
                value = tradeViewModel.price.value,               // ★
                onValueChange = {
                    tradeViewModel.price.value = it               // ★
                },
                label = { Text("Price") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }

        /* ---------- AMOUNT ---------- */
        OutlinedTextField(
            value = tradeViewModel.amount.value,                  // ★
            onValueChange = {
                tradeViewModel.amount.value = it                  // ★
            },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        /* ---------- SUBMIT BUTTON ---------- */
        Button(
            onClick = {
                val execType = if (orderType == "Market") "market" else "limit"
                tradeViewModel.updateSnapshot(snapshot)   // для market-цены
                tradeViewModel.place(
                    side      = if (isBuy) "buy" else "sell",
                    execType  = execType
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isBuy) "Buy" else "Sell")
        }


        /* Stop-loss + take-profit (commented for now, TBA) */
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Checkbox(checked = stopLossEnabled, onCheckedChange = { stopLossEnabled = it })
//            Text("Stop-Loss")
//        }
//        if (stopLossEnabled) {
//            OutlinedTextField(
//                value = stopLoss,
//                onValueChange = { stopLoss = it },
//                label = { Text("Stop-Loss Price") },
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                modifier = Modifier.fillMaxWidth()
//            )
//        }
//
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Checkbox(checked = takeProfitEnabled, onCheckedChange = { takeProfitEnabled = it })
//            Text("Take-Profit")
//        }
//        if (takeProfitEnabled) {
//            OutlinedTextField(
//                value = takeProfit,
//                onValueChange = { takeProfit = it },
//                label = { Text("Take-Profit Price") },
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                modifier = Modifier.fillMaxWidth()
//            )
//        }
    }
}

@Composable
fun DropdownMenuBox(
    selected: String,
    options: List<String>,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        TextButton(onClick = { expanded = true }) {
            Text(selected)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        expanded = false
                        onSelected(option)
                    }
                )
            }
        }
    }
}

@Composable
fun SegmentedButton(
    options: List<String>,
    selectedIndex: Int,
    onSelectedIndex: (Int) -> Unit
) {
    Row {
        options.forEachIndexed { index, label ->
            Button(
                onClick = { onSelectedIndex(index) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedIndex == index) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text(label)
            }
        }
    }
}
