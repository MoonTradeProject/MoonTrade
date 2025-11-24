package com.example.moontrade.ui.screens.main_screens.order_sub_screen

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.moontrade.R
import com.example.moontrade.ui.screens.components.glasskit.GlassCard
import com.example.moontrade.ui.theme.Violet300
import com.example.moontrade.ui.theme.Violet600

@Composable
fun MyOrdersCard(
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(22.dp)

    val transition = rememberInfiniteTransition(label = "ordersBorderGlow")
    val offset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 4000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ordersBorderOffset"
    )

    val borderBrush = Brush.horizontalGradient(
        colors = listOf(
            Color.Transparent,
            Violet300.copy(alpha = 0.8f),
            Violet600.copy(alpha = 0.2f),
            Color.Transparent
        ),
        startX = -600f + offset,
        endX = offset + 400f
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.4.dp, brush = borderBrush, shape = shape)
            .clip(shape)
            .clickable { onClick() }
    ) {
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            corner = 22.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_briefcase),
                        contentDescription = "Orders",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(36.dp)
                    )

                    Spacer(Modifier.width(14.dp))

                    Text(
                        text = "MY ORDERS",
                        style = MaterialTheme.typography.titleMedium,
                        color = cs.onSurface
                    )
                }

                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = "Go to Orders",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}
