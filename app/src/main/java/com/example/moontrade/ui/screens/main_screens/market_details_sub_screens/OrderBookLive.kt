//package com.example.moontrade.ui.screens.main_screens.market_details_sub_screens
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.blur
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.drawBehind
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.graphicsLayer
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import com.example.moontrade.model.OrderBookLevel
//import com.example.moontrade.model.OrderBookSnapshot
//
//
//@Composable
//fun OrderBookLive(snapshot: OrderBookSnapshot?, modifier: Modifier = Modifier) {
//    /////////////the following part TO DELETE/MODIFY later on//////////
//    val asks = snapshot?.asks
//        ?.sortedBy { it.price }
//        ?: emptyList()
//
//    val bids = snapshot?.bids
//        ?.sortedBy { it.price }
//        ?: emptyList()
//
//    val filledAsks = asks.toMutableList().apply {
//        val missing = 7 - size
//        repeat(if (missing > 0) missing else 0) {
//            add(OrderBookLevel(price = 0.0, volume = 0.0))
//        }
//    }
//
//    val filledBids = bids.toMutableList().apply {
//        val missing = 7 - size
//        repeat(if (missing > 0) missing else 0) {
//            add(OrderBookLevel(price = 0.0, volume = 0.0))
//        }
//    }
//    ///////////////////////////////////////////////////////////////
//
//    Column(
////        modifier = Modifier
////            .fillMaxWidth()
////            .clip(RoundedCornerShape(10.dp))
////            .background(Color.DarkGray.copy(alpha = 0.4f))
////            .padding(8.dp)
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(4.dp) // small outer spacing so glow is visible
//            .background(
//                brush = Brush.radialGradient(
//                    colors = listOf(
//                        Color(0xFFB400FF).copy(alpha = 0.25f), // strong neon outer glow
//                        Color.Transparent
//                    ),
//                    center = Offset.Zero,
//                    radius = 500f
//                ),
//                shape = RoundedCornerShape(20.dp)
//            )
//            .clip(RoundedCornerShape(20.dp))
//            .background(Color.Black.copy(alpha = 0.55f))
//            .border(
//                1.dp,
//                Color.White.copy(alpha = 0.1f),
//                RoundedCornerShape(20.dp)
//            )
//            .padding(12.dp)
//    ) {
//
//        Text("Order Book", style = MaterialTheme.typography.titleMedium)
//            Spacer(Modifier.height(8.dp))
//
//        Row(Modifier.fillMaxWidth()) {
//            Text("Price", modifier = Modifier.weight(1f), textAlign = TextAlign.Start, color = Color.Gray)
//            Text("Vol", modifier = Modifier.weight(1f), textAlign = TextAlign.End, color = Color.Gray)
//        }
//
//        Spacer(Modifier.height(4.dp))
//
//        // ASKS — sort by price ASCENDING, but draw REVERSED
//        filledAsks.take(7).reversed().forEach{
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                ) {
//                    PriceText(//price
//                        value = it.price,
//                        color = Color.Red,
//                        textAlign = TextAlign.Start,
//                        modifier = Modifier.weight(1f),
//                    )
//                    PriceText(//volume
//                        value = it.volume,
//                        color = Color.White,
//                        textAlign = TextAlign.End,
//                        modifier = Modifier.weight(1f),
//                    )
//                }
//                Spacer(modifier = Modifier.height(2.dp))
//            } ?: Text("Loading...", color = Color.Gray)
//
//        HorizontalDivider(Modifier.padding(vertical = 6.dp), thickness = 1.dp, color = Color.Gray)
//
//        // BIDS — sort by price DESCENDING
//        filledBids
//            .sortedByDescending { it.price }
//            .take(7)
//            .forEach {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                ) {
//                    PriceText(//price
//                        value = it.price,
//                        color = Color.Green,
//                        textAlign = TextAlign.Start,
//                        modifier = Modifier.weight(1f),
//                    )
//                    PriceText(//volume
//                        value = it.volume,
//                        color = Color.White,
//                        textAlign = TextAlign.End,
//                        modifier = Modifier.weight(1f),
//                    )
//                }
//                Spacer(modifier = Modifier.height(2.dp))
//            } ?: Text("Loading...", color = Color.Gray)
//    }
//    Spacer(modifier = Modifier.height(24.dp))
//
//}

package com.example.moontrade.ui.screens.main_screens.market_details_sub_screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moontrade.model.OrderBookLevel
import com.example.moontrade.model.OrderBookSnapshot

@Composable
fun OrderBookLive(
    snapshot: OrderBookSnapshot?,
    modifier: Modifier = Modifier
) {
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

    val containerShape = RoundedCornerShape(20.dp)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFB400FF).copy(alpha = 0.23f),
                        Color.Transparent
                    ),
                    center = Offset.Zero,
                    radius = 520f
                ),
                shape = containerShape
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.08f),
                shape = containerShape
            )
            .background(Color(0xFF05050A).copy(alpha = 0.85f), containerShape)
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        // Заголовок
        Text(
            text = "Order Book",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFFEDEDF8)
        )

        Spacer(Modifier.height(8.dp))

        // Хедер колонок
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Price",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF777B92)
            )
            Text(
                text = "Vol",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF777B92)
            )
        }

        Spacer(Modifier.height(4.dp))

        // ASKS (sell)
        filledAsks
            .take(7)
            .reversed()
            .forEach { level ->
                OrderBookRow(
                    price = level.price,
                    volume = level.volume,
                    priceColor = Color(0xFFFF4A4A),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(2.dp))
            }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 6.dp),
            thickness = 1.dp,
            color = Color(0xFF262738)
        )

        // BIDS (buy)
        filledBids
            .sortedByDescending { it.price }
            .take(7)
            .forEach { level ->
                OrderBookRow(
                    price = level.price,
                    volume = level.volume,
                    priceColor = Color(0xFF16B36A),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(2.dp))
            }

        Spacer(modifier = Modifier.height(8.dp))
    }

    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun OrderBookRow(
    price: Double,
    volume: Double,
    priceColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        PriceText(
            value = price,
            color = priceColor,
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.SemiBold
        )
        PriceText(
            value = volume,
            color = Color(0xFFEDEDF8),
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun PriceText(
    value: Double,
    color: Color,
    textAlign: TextAlign,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight = FontWeight.Medium
) {
    val text = if (value == 0.0) "" else value.toString()

    Text(
        text = text,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        maxLines = 1,
        style = TextStyle(
            fontSize = 13.sp,
            fontWeight = fontWeight,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 0.2.sp,
            fontFeatureSettings = "tnum" // табличные цифры, чтобы столбцы не скакали
        )
    )
}
