package com.example.moontrade.ui.screens.main_screens.market_details_sub_screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moontrade.model.OrderBookSnapshot
import com.example.moontrade.utils.PriceCounter
import com.example.moontrade.viewmodels.TradeViewModel

@Composable
fun TradeForm(
    tradeViewModel: TradeViewModel,
    snapshot: OrderBookSnapshot?,
    modifier: Modifier = Modifier
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
        Spacer(Modifier.height(8.dp))
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
        Spacer(Modifier.height(8.dp))

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

        Text(
            text = price.takeIf { it != 0.0 }?.toString() ?: "",
            fontSize = 12.sp
        )

    }
}

@Composable
fun DropdownMenuBox(
    selected: String,
    options: List<String>,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var boxWidth by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
            .onGloballyPositioned{ coordinates ->
                boxWidth = coordinates.size.width // px
            }
            .clickable { expanded = true }

            .padding(vertical = 12.dp),

        contentAlignment = Alignment.CenterStart
    ) {

        Text(
            text = selected,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "dropdown arrow",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(25.dp)
                .padding(end = 4.dp)
                .rotate(if (expanded) 180f else 0f) // optional rotation
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.DarkGray.copy(alpha = 0.4f))
                .width(with(LocalDensity.current) { boxWidth.toDp() })
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            option,
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            color = if (option == selected) Color(0xFF47299B) else Color.White,
                            modifier = Modifier.fillMaxWidth()
                        )
                           },
                    onClick = {
                        expanded = false
                        onSelected(option)
                    },

                )
            }
        }
    }
}
