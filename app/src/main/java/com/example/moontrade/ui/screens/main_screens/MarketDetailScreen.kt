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
import androidx.navigation.NavController
import com.example.moontrade.model.OrderBookSnapshot
import com.example.moontrade.ui.screens.components.orderbook.OrderBookLive
import com.example.moontrade.viewmodels.MarketDetailViewModel

@Composable
fun MarketDetailScreen(
    navController: NavController,
    symbol: String,
    viewModel: MarketDetailViewModel
) {
    // UI state
    var orderType by remember { mutableStateOf("Limit") }
    var isBuy by remember { mutableStateOf(true) }
    var price by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var stopLossEnabled by remember { mutableStateOf(false) }
    var stopLoss by remember { mutableStateOf("") }
    var takeProfitEnabled by remember { mutableStateOf(false) }
    var takeProfit by remember { mutableStateOf("") }

    // Subscribe to WS snapshot
    val snapshot by viewModel.snapshot.collectAsState()

    // Connect to WS on enter
    LaunchedEffect(symbol) {
        viewModel.connect(symbol)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = symbol,
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            "Best Bid: ${snapshot?.bids?.firstOrNull()?.price ?: "-"}    |    " +
                    "Best Ask: ${snapshot?.asks?.firstOrNull()?.price ?: "-"}"
        )

        Spacer(modifier = Modifier.height(12.dp))

        OrderBookLive(snapshot = snapshot)

        Spacer(modifier = Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Order Type:")
            Spacer(Modifier.width(8.dp))
            DropdownMenuBox(
                selected = orderType,
                options = listOf("Limit", "Market"),
                onSelected = { orderType = it }
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Side:")
            Spacer(Modifier.width(8.dp))
            SegmentedButton(
                options = listOf("Buy", "Sell"),
                selectedIndex = if (isBuy) 0 else 1,
                onSelectedIndex = { isBuy = it == 0 }
            )
        }

        if (orderType == "Limit") {
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = stopLossEnabled, onCheckedChange = { stopLossEnabled = it })
            Text("Stop-Loss")
        }

        if (stopLossEnabled) {
            OutlinedTextField(
                value = stopLoss,
                onValueChange = { stopLoss = it },
                label = { Text("Stop-Loss Price") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = takeProfitEnabled, onCheckedChange = { takeProfitEnabled = it })
            Text("Take-Profit")
        }

        if (takeProfitEnabled) {
            OutlinedTextField(
                value = takeProfit,
                onValueChange = { takeProfit = it },
                label = { Text("Take-Profit Price") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                println("[order] $orderType ${if (isBuy) "Buy" else "Sell"} $amount @ $price")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isBuy) "Buy" else "Sell")
        }
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
