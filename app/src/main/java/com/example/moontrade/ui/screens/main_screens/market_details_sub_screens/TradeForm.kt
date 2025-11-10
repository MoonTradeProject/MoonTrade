package com.example.moontrade.ui.screens.main_screens.market_details_sub_screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moontrade.model.OrderBookSnapshot
import com.example.moontrade.viewmodels.TradeViewModel

@Composable
fun TradeForm(
    tradeViewModel: TradeViewModel,
    snapshot: OrderBookSnapshot?,
    modifier: Modifier = Modifier
) {


    var orderType by remember { mutableStateOf("Limit") }
    var isBuy by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        /* ---------- ORDER TYPE ---------- */
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Order Type:")
            Spacer(Modifier.width(8.dp))
            DropdownMenuBox(
                selected = orderType,
                options = listOf("Limit", "Market"),
                onSelected = { orderType = it }
            )
        }

        Spacer(Modifier.height(8.dp))

        /* ---------- SIDE ---------- */
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Side:")
            Spacer(Modifier.width(8.dp))
            SegmentedButton(
                options = listOf("Buy", "Sell"),
                selectedIndex = if (isBuy) 0 else 1,
                onSelectedIndex = { isBuy = it == 0 }
            )
        }

        Spacer(Modifier.height(8.dp))

        /* ---------- PRICE (only for Limit) ---------- */
        if (orderType == "Limit") {
            OutlinedTextField(
                value = tradeViewModel.price.value,
                onValueChange = { tradeViewModel.price.value = it },
                label = { Text("Price") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
        }

        /* ---------- AMOUNT ---------- */
        OutlinedTextField(
            value = tradeViewModel.amount.value,
            onValueChange = { tradeViewModel.amount.value = it },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        /* ---------- SUBMIT BUTTON ---------- */
        Button(
            onClick = {
                val execType = if (orderType == "Market") "market" else "limit"
                tradeViewModel.updateSnapshot(snapshot)
                tradeViewModel.place(
                    side = if (isBuy) "buy" else "sell",
                    execType = execType
                )
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
