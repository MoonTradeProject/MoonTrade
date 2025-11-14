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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moontrade.model.OrderBookSnapshot
import com.example.moontrade.viewmodels.TradeViewModel

@Composable
fun TradeForm(
    tradeViewModel: TradeViewModel,
    snapshot: OrderBookSnapshot?,
    modifier: Modifier = Modifier
) {


    var orderType by remember { mutableStateOf("Market") }
    var isBuy by remember { mutableStateOf(true) }


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


        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Order Type:")
            Spacer(Modifier.width(8.dp))
            DropdownMenuBox(
                selected = orderType,
                options = listOf("Market", "Limit"),
                onSelected = { orderType = it }
            )
        }


        /* ---------- PRICE (only for Limit) ---------- */
        if (orderType == "Limit") {
            OutlinedTextField(
                value = tradeViewModel.price.value,
                onValueChange = { tradeViewModel.price.value = it },
                placeholder  = { Text("Price") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,

            )
        }
        Spacer(Modifier.height(5.dp))
        /* ---------- AMOUNT ---------- */
        OutlinedTextField(
            value = tradeViewModel.amount.value,
            onValueChange = { tradeViewModel.amount.value = it },
            placeholder = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
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

//@Composable
//fun SegmentedButton(
//    options: List<String>,
//    selectedIndex: Int,
//    onSelectedIndex: (Int) -> Unit
//) {
//    Row {
//        options.forEachIndexed { index, label ->
//            Button(
//                onClick = { onSelectedIndex(index) },
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = if (selectedIndex == index) MaterialTheme.colorScheme.primary
//                    else MaterialTheme.colorScheme.surfaceVariant
//                ),
//                modifier = Modifier.weight(1f)
//            ) {
//                Text(label)
//            }
//        }
//    }
//}
