package com.example.moontrade.ui.screens.main_screens.market_details_sub_screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moontrade.model.OrderBookSnapshot
import com.example.moontrade.viewmodels.TradeViewModel
import com.example.moontrade.viewmodels.UserAssetsViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun TradeForm(
    tradeViewModel: TradeViewModel,
    snapshot: OrderBookSnapshot?,
    assetBalance: String,
    onExecuteTrade: (execType: String, side: String) -> Unit,
    userAssetsViewModel: UserAssetsViewModel,
    orderType: String,
    isBuy: Boolean,
    price: Double,
    onOrderTypeChange: (String) -> Unit,
    onBuySellChange: (Boolean) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
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
                Text(
                    text = "Balance: ",
                    fontSize = 12.sp,
                    color = Color(0xFFB8BBDA)
                )
                Text(
                    text = assetBalance,
                    fontSize = 12.sp,
                    color = Color(0xFFE5E6FA)
                )
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

            // LIMIT price input
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

            // AMOUNT input
            OutlinedTextField(
                value = tradeViewModel.amount.value,
                onValueChange = { newValue ->
                    tradeViewModel.amount.value = newValue
                },
                placeholder = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
            )

            Spacer(Modifier.height(8.dp))

            TradeActionButton(
                isBuy = isBuy,
                orderType = orderType,
                price = price,
                modifier = Modifier.fillMaxWidth(),
                onExecuteTrade = onExecuteTrade
            )
        }
    }
}
