package com.example.moontrade.ui.screens.main_screens.trade_sub_screens


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BuyTab() {
    var amount by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Buy Crypto", style = MaterialTheme.typography.titleMedium)

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount in USDT") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { /* TODO: execute buy */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Buy")
        }
    }
}
