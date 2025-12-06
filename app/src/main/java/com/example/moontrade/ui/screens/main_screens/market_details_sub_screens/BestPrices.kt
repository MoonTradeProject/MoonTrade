package com.example.moontrade.ui.screens.main_screens.market_details_sub_screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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

    val askColor = Color(0xFFFF4C4C)   // такой же, как в OrderBook asks
    val bidColor = Color(0xFF2DFF88)   // такой же, как в OrderBook bids

    val bestAsk = snapshot?.asks?.minByOrNull { it.price }?.price
    val bestBid = snapshot?.bids?.maxByOrNull { it.price }?.price

    Column(
        modifier = Modifier.padding(bottom = 4.dp)
    ) {
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(color = Color(0xFFB8BBDA), fontSize = 15.sp)) {
                    append("Best Ask: ")
                }
                withStyle(
                    SpanStyle(
                        color = askColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                ) {
                    append(bestAsk?.let { "%.4f".format(it) } ?: "-")
                }
            }
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(color = Color(0xFFB8BBDA), fontSize = 15.sp)) {
                    append("Best Bid: ")
                }
                withStyle(
                    SpanStyle(
                        color = bidColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                ) {
                    append(bestBid?.let { "%.4f".format(it) } ?: "-")
                }
            }
        )
    }
}


