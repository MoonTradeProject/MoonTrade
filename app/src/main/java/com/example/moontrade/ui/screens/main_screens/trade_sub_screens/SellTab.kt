package com.example.moontrade.ui.screens.main_screens.trade_sub_screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moontrade.viewmodels.TradeViewModel

@Composable
fun SellTab(vm: TradeViewModel = hiltViewModel()) {
    Column(Modifier.padding(16.dp)) {

        OutlinedTextField(
            value = vm.amount.value,
            onValueChange = { vm.amount.value = it },
            label = { Text("Amount (${vm.assetName.value})") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { vm.place("Sell") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sell @ ${vm.lastBid}")
        }
    }
}
