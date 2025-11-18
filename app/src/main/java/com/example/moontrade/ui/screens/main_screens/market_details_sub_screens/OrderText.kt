package com.example.moontrade.ui.screens.main_screens.market_details_sub_screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.graphics.Color

@Composable
fun PriceText(
    value: Double,
    color: Color,
    textAlign: TextAlign,
    modifier: Modifier = Modifier,
    decimals: Int = 4
) {
    val integerPartLength = value.toInt().toString().length

    val adjustedDecimals = if (integerPartLength >= 3) 1 else decimals

    val safeDecimals = adjustedDecimals.coerceIn(0, 8)

    val formatted = "%.${safeDecimals}f".format(value)

    val fontSize = when {
        formatted.length > 12 -> 12.sp
        formatted.length > 8 -> 14.sp
        else -> 16.sp
    }

    Text(
        text = formatted,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        maxLines = 1,
        softWrap = false,
        overflow = TextOverflow.Clip,
        fontSize = fontSize
    )
}




