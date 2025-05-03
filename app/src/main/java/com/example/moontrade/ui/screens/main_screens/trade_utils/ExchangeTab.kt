package com.example.moontrade.ui.screens.main_screens.trade_utils


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ExchangeTab() {
    var from by remember { mutableStateOf("") }
    var to by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Exchange Crypto", style = MaterialTheme.typography.titleMedium)

        OutlinedTextField(
            value = from,
            onValueChange = { from = it },
            label = { Text("From (e.g. BTC)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = to,
            onValueChange = { to = it },
            label = { Text("To (e.g. ETH)") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { /* TODO: execute exchange */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Exchange")
        }
    }
}
