package com.example.moontrade.ui.screens.main_screens.market_details_sub_screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moontrade.model.OrderBookSnapshot


@Composable
fun BestPrices(snapshot: OrderBookSnapshot?) {
    val bestAsk = snapshot?.asks?.firstOrNull()?.price
    val bestBid = snapshot?.bids?.firstOrNull()?.price

    Column {
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
                    append("Best ask: ")
                }
                withStyle(SpanStyle(color = Color.Red, fontWeight = FontWeight.Normal)) {
                    append(bestAsk?.toString() ?: "-")
                }
            },
            fontSize = 16.sp
        )
        Spacer(Modifier.height(4.dp))
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
                    append("Best bid: ")
                }
                withStyle(SpanStyle(color = Color.Green, fontWeight = FontWeight.Normal)) {
                    append(bestBid?.toString() ?: "-")
                }
            },
            fontSize = 16.sp
        )
    }
}

