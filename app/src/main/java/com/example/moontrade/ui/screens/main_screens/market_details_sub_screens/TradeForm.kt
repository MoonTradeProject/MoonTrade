package com.example.moontrade.ui.screens.main_screens.market_details_sub_screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moontrade.model.OrderBookSnapshot
import com.example.moontrade.utils.PriceCounter
import com.example.moontrade.viewmodels.TradeViewModel
import com.example.moontrade.viewmodels.UserAssetsViewModel

@Composable
fun TradeForm(
    tradeViewModel: TradeViewModel,
    snapshot: OrderBookSnapshot?,
    assetBalance: String,
    userAssetsViewModel: UserAssetsViewModel,
    modifier: Modifier = Modifier,
) {


    var orderType by remember { mutableStateOf("Market") }
    var isBuy by remember { mutableStateOf(true) }
    val price = PriceCounter(snapshot, tradeViewModel.amount.value, if (isBuy) "buy" else "sell")


    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        //best bid best ask area
        BestPrices(snapshot = snapshot)

        Spacer(Modifier.height(8.dp))

        //buy sell slider
        AnimatedSegmentedButton(
            options = listOf("Buy", "Sell"),
            selectedIndex = if (isBuy) 0 else 1,
            onSelectedIndex = { isBuy = it == 0 },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Balance: ")
            Text(assetBalance)
        }

        Spacer(Modifier.height(8.dp))

        DropdownMenuBox(
            selected = orderType,
            options = listOf("Market", "Limit"),
            onSelected = { orderType = it }
        )

        Spacer(Modifier.height(8.dp))


        /* ---------- PRICE (only for Limit) ---------- */
        if (orderType == "Limit") {
            OutlinedTextField(
                value = tradeViewModel.price.value,
                onValueChange = { tradeViewModel.price.value = it },
                placeholder  = { Text("Price") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
            )
            Spacer(Modifier.height(8.dp))
        }
//        Spacer(Modifier.height(8.dp))
        /* ---------- AMOUNT ---------- */
        OutlinedTextField(
            value = tradeViewModel.amount.value,
            onValueChange = { tradeViewModel.amount.value = it },
            placeholder = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
        )

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

        /* ---------- SUBMIT BUTTON ---------- */
        Button(
            onClick = {
                val execType = if (orderType == "Market") "market" else "limit"
                tradeViewModel.updateSnapshot(snapshot)
                userAssetsViewModel.loadUserAssets()
                tradeViewModel.place(
                    side = if (isBuy) "buy" else "sell",
                    execType = execType,
                    userAssetsViewModel = userAssetsViewModel
                )
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
