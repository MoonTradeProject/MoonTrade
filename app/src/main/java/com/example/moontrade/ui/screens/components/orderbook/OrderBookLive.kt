package com.example.moontrade.ui.screens.components.orderbook

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.moontrade.model.OrderBookSnapshot

@Composable
fun OrderBookLive(snapshot: OrderBookSnapshot?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray.copy(alpha = 0.4f))
            .padding(8.dp)
    ) {
        Text("Order Book", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        Row(Modifier.fillMaxWidth()) {
            Text("Vol", modifier = Modifier.weight(1f), textAlign = TextAlign.Start, color = Color.Gray)
            Text("Price", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, color = Color.Gray)
            Text("Vol", modifier = Modifier.weight(1f), textAlign = TextAlign.End, color = Color.Gray)
        }

        Spacer(Modifier.height(4.dp))

        // 🔺 ASKS — sort by price ASCENDING, but draw REVERSED
        snapshot?.asks
            ?.sortedBy { it.price }
            ?.reversed()
            ?.take(20)
            ?.forEach {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "%.2f".format(it.price),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                    Text(
                        text = "%.4f".format(it.volume),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End,
                        color = Color.Red
                    )
                }
            } ?: Text("Loading...", color = Color.Gray)

        HorizontalDivider(Modifier.padding(vertical = 6.dp), thickness = 1.dp, color = Color.Gray)

        // 🟢 BIDS — sort by price DESCENDING
        snapshot?.bids
            ?.sortedByDescending { it.price }
            ?.take(20)
            ?.forEach {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "%.4f".format(it.volume),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Start,
                        color = Color.Green
                    )
                    Text(
                        text = "%.2f".format(it.price),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                    Spacer(Modifier.weight(1f))
                }
            }
    }
}
