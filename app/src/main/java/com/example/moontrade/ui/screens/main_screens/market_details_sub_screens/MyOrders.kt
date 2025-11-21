package com.example.moontrade.ui.screens.main_screens.market_details_sub_screens

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun MyOrders(){
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = false,
            onCheckedChange = {}
        )
        Text(
            "TP/SL"
        )
    }
}