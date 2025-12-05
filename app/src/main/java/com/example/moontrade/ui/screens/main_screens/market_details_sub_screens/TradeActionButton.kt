package com.example.moontrade.ui.screens.main_screens.market_details_sub_screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moontrade.ui.shaders.ShaderFillBox
import com.example.moontrade.ui.shaders.createBuyMetallicShader
import com.example.moontrade.ui.shaders.createSellMetallicShader
import com.example.moontrade.ui.shaders.rememberShaderTime

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun TradeActionButton(
    isBuy: Boolean,
    orderType: String,
    price: Double,
    modifier: Modifier = Modifier,
    onExecuteTrade: (execType: String, side: String) -> Unit
) {
    val execType = if (orderType == "Market") "market" else "limit"
    val side = if (isBuy) "buy" else "sell"

    val shader = remember(isBuy) {
        if (isBuy) {
            createBuyMetallicShader()
        } else {
            createSellMetallicShader()
        }
    }

    val shaderTime = rememberShaderTime()

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(
            dampingRatio = 0.7f,
            stiffness = 300f
        ),
        label = "tradeButtonScale"
    )

    val buttonShape = RoundedCornerShape(18.dp)

    Box(
        modifier = modifier
            .height(52.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(buttonShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onExecuteTrade(execType, side)
            }
    ) {
        ShaderFillBox(
            modifier = Modifier
                .matchParentSize(),
            shader = shader,
            time = shaderTime
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(buttonShape)
                .graphicsLayer {
                    alpha = 0.96f
                }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isBuy) "Buy" else "Sell",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                if (price != 0.0) {
                    Text(
                        text = price.toString(),
                        color = Color.White.copy(alpha = 0.82f),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
