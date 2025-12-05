package com.example.moontrade.ui.screens.main_screens.market_details_sub_screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.moontrade.model.OrderBookLevel
import com.example.moontrade.model.OrderBookSnapshot


@Composable
fun OrderBookLive(snapshot: OrderBookSnapshot?, modifier: Modifier = Modifier) {
    /////////////the following part TO DELETE/MODIFY later on//////////
    val asks = snapshot?.asks
        ?.sortedBy { it.price }
        ?: emptyList()

    val bids = snapshot?.bids
        ?.sortedBy { it.price }
        ?: emptyList()

    val filledAsks = asks.toMutableList().apply {
        val missing = 7 - size
        repeat(if (missing > 0) missing else 0) {
            add(OrderBookLevel(price = 0.0, volume = 0.0))
        }
    }

    val filledBids = bids.toMutableList().apply {
        val missing = 7 - size
        repeat(if (missing > 0) missing else 0) {
            add(OrderBookLevel(price = 0.0, volume = 0.0))
        }
    }
    ///////////////////////////////////////////////////////////////

    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(10.dp))
//            .background(Color.DarkGray.copy(alpha = 0.4f))
//            .padding(8.dp)
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp) // small outer spacing so glow is visible
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFB400FF).copy(alpha = 0.25f), // strong neon outer glow
                        Color.Transparent
                    ),
                    center = Offset.Zero,
                    radius = 500f
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Black.copy(alpha = 0.55f))
            .border(
                1.dp,
                Color.White.copy(alpha = 0.1f),
                RoundedCornerShape(20.dp)
            )
            .padding(12.dp)
    ) {

        Text("Order Book", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

        Row(Modifier.fillMaxWidth()) {
            Text("Price", modifier = Modifier.weight(1f), textAlign = TextAlign.Start, color = Color.Gray)
            Text("Vol", modifier = Modifier.weight(1f), textAlign = TextAlign.End, color = Color.Gray)
        }

        Spacer(Modifier.height(4.dp))

        // ASKS — sort by price ASCENDING, but draw REVERSED
        filledAsks.take(7).reversed().forEach{
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    PriceText(//price
                        value = it.price,
                        color = Color.Red,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(1f),
                    )
                    PriceText(//volume
                        value = it.volume,
                        color = Color.White,
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1f),
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
            } ?: Text("Loading...", color = Color.Gray)

        HorizontalDivider(Modifier.padding(vertical = 6.dp), thickness = 1.dp, color = Color.Gray)

        // BIDS — sort by price DESCENDING
        filledBids
            .sortedByDescending { it.price }
            .take(7)
            .forEach {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    PriceText(//price
                        value = it.price,
                        color = Color.Green,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(1f),
                    )
                    PriceText(//volume
                        value = it.volume,
                        color = Color.White,
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1f),
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
            } ?: Text("Loading...", color = Color.Gray)
    }
    Spacer(modifier = Modifier.height(24.dp))

}
