//package com.example.moontrade.ui.screens.main_screens.market_details_sub_screens
//
//import androidx.compose.animation.core.animateDpAsState
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//
//@Composable
//fun AnimatedSegmentedButton(
//    options: List<String> = listOf("Buy", "Sell"),
//    selectedIndex: Int,
//    onSelectedIndex: (Int) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val indicatorPadding = 4.dp
//    val indicatorWidth = 101.dp // adjust as needed
//    val sliderColor = if (options[selectedIndex].lowercase() == "buy") Color(0xFF0BA200) else Color(0xFFB70000)
//
//    // animate the horizontal position of the indicator
//    val offsetX by animateDpAsState(targetValue = (indicatorWidth - 5.dp) * selectedIndex)
//
//    Box(
//        modifier = modifier
//            .height(40.dp)
//            .clip(RoundedCornerShape(20.dp))
//            .background(Color.LightGray.copy(alpha = 0.3f))
//    ) {
//        Box(
//            modifier = Modifier
//                .offset(x = offsetX)
//                .width(indicatorWidth)
//                .fillMaxHeight()
//                .padding(all = indicatorPadding)
//                .clip(RoundedCornerShape(20.dp))
//                .background(sliderColor) // active color
//        )
//
//        Row(modifier = Modifier.fillMaxSize()) {
//            options.forEachIndexed { index, option ->
//                Box(
//                    modifier = Modifier
//                        .weight(1f)
//                        .fillMaxHeight().fillMaxWidth()
//                        .clickable { onSelectedIndex(index) },
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = option,
//                        color = if (selectedIndex == index) Color(0xFFCECECE) else Color.Black,
//                        fontSize = 14.sp,
//                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
//                    )
//                }
//            }
//        }
//    }
//}
//
//
//
//package com.example.moontrade.ui.screens.main_screens.market_details_sub_screens
//
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.compose.animation.core.animateDpAsState
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.offset
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.moontrade.ui.shaders.ShaderFillBox
//import com.example.moontrade.ui.shaders.createBuyShader
//import com.example.moontrade.ui.shaders.createSellShader
//import com.example.moontrade.ui.shaders.rememberShaderTime
//
//@RequiresApi(Build.VERSION_CODES.TIRAMISU)
//@Composable
//fun AnimatedSegmentedButton(
//    options: List<String> = listOf("Buy", "Sell"),
//    selectedIndex: Int,
//    onSelectedIndex: (Int) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val indicatorPadding = 4.dp
//    val indicatorWidth = 101.dp
//
//    val isBuySelected = options.getOrNull(selectedIndex)
//        ?.equals("buy", ignoreCase = true) == true
//
//    val offsetX by animateDpAsState(
//        targetValue = (indicatorWidth - 5.dp) * selectedIndex,
//        label = "indicatorOffset"
//    )
//
//    val shader = remember(isBuySelected) {
//        if (isBuySelected) {
//            createBuyShader()
//        } else {
//            createSellShader()
//        }
//    }
//
//    val shaderTime = rememberShaderTime()
//
//    Box(
//        modifier = modifier
//            .height(40.dp)
//            .clip(RoundedCornerShape(20.dp))
//            .background(Color.LightGray.copy(alpha = 0.3f))
//    ) {
//        Box(
//            modifier = Modifier
//                .offset(x = offsetX)
//                .width(indicatorWidth)
//                .fillMaxHeight()
//                .padding(all = indicatorPadding)
//                .clip(RoundedCornerShape(20.dp))
//        ) {
//            ShaderFillBox(
//                modifier = Modifier.fillMaxSize(),
//                shader = shader,
//                time = shaderTime
//            )
//        }
//
//        Row(modifier = Modifier.fillMaxSize()) {
//            options.forEachIndexed { index, option ->
//                Box(
//                    modifier = Modifier
//                        .weight(1f)
//                        .fillMaxHeight()
//                        .clickable { onSelectedIndex(index) },
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = option,
//                        color = if (selectedIndex == index) Color(0xFFCECECE) else Color.Black,
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//            }
//        }
//    }
//}


package com.example.moontrade.ui.screens.main_screens.market_details_sub_screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moontrade.ui.shaders.ShaderFillBox
import com.example.moontrade.ui.shaders.createBuyMetallicShader
import com.example.moontrade.ui.shaders.createSellMetallicShader
import com.example.moontrade.ui.shaders.rememberShaderTime

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AnimatedSegmentedButton(
    options: List<String> = listOf("Buy", "Sell"),
    selectedIndex: Int,
    onSelectedIndex: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val trackShape = RoundedCornerShape(999.dp)
    val interactionSource = remember { MutableInteractionSource() }

    BoxWithConstraints(
        modifier = modifier
            .height(44.dp)
            .clip(trackShape)
            .background(Color(0xFF05060B))
            .padding(horizontal = 4.dp, vertical = 3.dp)
    ) {
        val segmentCount = options.size.coerceAtLeast(1)
        val segmentWidth = maxWidth / segmentCount

        val isBuySelected = options
            .getOrNull(selectedIndex)
            ?.equals("buy", ignoreCase = true) == true

        val indicatorOffset by animateDpAsState(
            targetValue = segmentWidth * selectedIndex,
            label = "indicatorOffset"
        )

        // ТЕПЕРЬ ИНДИКАТОР ТОЖЕ METALLIC, КАК TRADE КНОПКА
        val shader = remember(isBuySelected) {
            if (isBuySelected) {
                createBuyMetallicShader()
            } else {
                createSellMetallicShader()
            }
        }

        val shaderTime = rememberShaderTime()

        // Металлический ползунок
        Box(
            modifier = Modifier
                .offset(x = indicatorOffset)
                .width(segmentWidth)
                .fillMaxHeight()
                .clip(trackShape)
        ) {
            ShaderFillBox(
                modifier = Modifier.fillMaxSize(),
                shader = shader,
                time = shaderTime
            )
        }

        // Текстовые сегменты сверху
        Row(modifier = Modifier.fillMaxSize()) {
            options.forEachIndexed { index, option ->
                val isSelected = index == selectedIndex

                val textColor = when {
                    isSelected && option.equals("buy", ignoreCase = true) -> Color(0xFFE3FFE9)
                    isSelected && option.equals("sell", ignoreCase = true) -> Color(0xFFFFE4E4)
                    else -> Color(0xFFB4B4C3)
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null // без белой ряби/рипла
                        ) {
                            onSelectedIndex(index)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = option,
                        color = textColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}



