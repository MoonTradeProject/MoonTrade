package com.example.moontrade.ui.screens.main_screens.market_details_sub_screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.moontrade.model.OrderBookSnapshot
import com.example.moontrade.utils.PriceCounter
import com.example.moontrade.viewmodels.OrdersViewModel
import com.example.moontrade.viewmodels.TradeViewModel
import com.example.moontrade.viewmodels.UserAssetsViewModel

@Composable
fun TradeForm(
    tradeViewModel: TradeViewModel,
    snapshot: OrderBookSnapshot?,
    assetBalance: String,
    onExecuteTrade: (execType: String, side: String) -> Unit,
    userAssetsViewModel: UserAssetsViewModel,
    orderType: String,
    isBuy: Boolean,
    price: Any,
    onOrderTypeChange: (String) -> Unit,
    onBuySellChange: (Boolean) -> Unit,
    onPriceChange: (String) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier,
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            BestPrices(snapshot = snapshot)
            Spacer(Modifier.height(8.dp))

            AnimatedSegmentedButton(
                options = listOf("Buy", "Sell"),
                selectedIndex = if (isBuy) 0 else 1,
                onSelectedIndex = { index ->
                    onBuySellChange(index == 0)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Balance: ")
                Text(assetBalance)
            }

            Spacer(Modifier.height(8.dp))

            DropdownMenuBox(
                selected = orderType,
                options = listOf("Market", "Limit"),
                onSelected = { newType ->
                    onOrderTypeChange(newType)
                }
            )

            Spacer(Modifier.height(8.dp))

            if (orderType == "Limit") {
                OutlinedTextField(
                    value = tradeViewModel.price.value,
                    onValueChange = { tradeViewModel.price.value = it },
                    placeholder = { Text("Price") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                )
                Spacer(Modifier.height(8.dp))
            }

            OutlinedTextField(
                value = tradeViewModel.amount.value,
                onValueChange = { tradeViewModel.amount.value = it },
                placeholder = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
            )
            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    val execType = if (orderType == "Market") "market" else "limit"
                    val side = if (isBuy) "buy" else "sell"

                    onExecuteTrade(execType, side)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isBuy) "Buy" else "Sell")
            }

            Text(
                text = price.takeIf { it != 0.0 }?.toString() ?: "",
                fontSize = 16.sp
            )
        }
    }

}
