package com.example.moontrade.ui.screens.main_screens.market_details_sub_screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AnimatedSegmentedButton(
    options: List<String> = listOf("Buy", "Sell"),
    selectedIndex: Int,
    onSelectedIndex: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val indicatorPadding = 4.dp
    val indicatorWidth = 101.dp // adjust as needed
    val sliderColor = if (options[selectedIndex].lowercase() == "buy") Color(0xFF0BA200) else Color(0xFFB70000)

    // animate the horizontal position of the indicator
    val offsetX by animateDpAsState(targetValue = (indicatorWidth - 5.dp) * selectedIndex)

    Box(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.LightGray.copy(alpha = 0.3f))
    ) {
        Box(
            modifier = Modifier
                .offset(x = offsetX)
                .width(indicatorWidth)
                .fillMaxHeight()
                .padding(all = indicatorPadding)
                .clip(RoundedCornerShape(20.dp))
                .background(sliderColor) // active color
        )

        Row(modifier = Modifier.fillMaxSize()) {
            options.forEachIndexed { index, option ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight().fillMaxWidth()
                        .clickable { onSelectedIndex(index) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = option,
                        color = if (selectedIndex == index) Color(0xFFCECECE) else Color.Black,
                        fontSize = 14.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                }
            }
        }
    }
}



