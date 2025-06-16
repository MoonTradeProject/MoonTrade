package com.example.moontrade.ui.screens.components.orderbook

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp





data class OrderBookLevel(val price: String, val volume: String)

@Composable
fun OrderBookMock() {
    val asks = listOf(
        OrderBookLevel("169.01", "1"),
        OrderBookLevel("168.31", "120"),
        OrderBookLevel("165.02", "405"),
        OrderBookLevel("165.00", "5988"),
        OrderBookLevel("164.99", "74897")
    )

    val bids = listOf(
        OrderBookLevel("164.96", "656"),
        OrderBookLevel("164.94", "1541"),
        OrderBookLevel("164.92", "3234"),
        OrderBookLevel("164.91", "954"),
        OrderBookLevel("164.89", "5013")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray.copy(alpha = 0.4f))
            .padding(8.dp)
    ) {
        Text("Order Book", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        // 🔹 Шапка Vol | Price | Vol
        Row(Modifier.fillMaxWidth()) {
            Text("Vol", modifier = Modifier.weight(1f), textAlign = TextAlign.Start, color = Color.Gray)
            Text("Price", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, color = Color.Gray)
            Text("Vol", modifier = Modifier.weight(1f), textAlign = TextAlign.End, color = Color.Gray)
        }

        Spacer(Modifier.height(4.dp))


        asks.forEach {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.weight(1f))
                Text(
                    text = it.price,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
                Text(
                    text = it.volume,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End,
                    color = Color.Red
                )
            }
        }

        HorizontalDivider(Modifier.padding(vertical = 6.dp), thickness = 1.dp, color = Color.Gray)

        // 🟢 Покупатели (bids) снизу вверх
        bids.reversed().forEach {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = it.volume,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start,
                    color = Color.Green
                )
                Text(
                    text = it.price,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
                Spacer(Modifier.weight(1f)) // справа пусто
            }
        }
    }
}

