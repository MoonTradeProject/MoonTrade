package com.example.moontrade.ui.screens.main_screens.trade_utils

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BalanceHeader(
    userBalance: Double,
    selectedCurrency: String
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Available Balance", style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.height(4.dp))
            Text("$userBalance $selectedCurrency", style = MaterialTheme.typography.headlineSmall)
        }
    }
}
