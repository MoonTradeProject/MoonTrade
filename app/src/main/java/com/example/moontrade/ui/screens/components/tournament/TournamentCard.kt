package com.example.moontrade.ui.screens.components.tournament

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import com.example.moontrade.ui.screens.components.buttons.PrimaryGradientButton
import com.example.moontrade.ui.screens.components.glasskit.GlassCard
import com.example.moontrade.ui.theme.Violet300
import com.example.moontrade.ui.theme.Violet600

@Composable
fun TournamentCard(
    title: String,
    subtitle: String,
    isJoined: Boolean,
    actionText: String,
    onAction: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(20.dp)

    // анимированный бордер — только если not joined
    val borderModifier = if (!isJoined) {
        val transition = rememberInfiniteTransition()
        val offset by transition.animateFloat(
            initialValue = 0f,
            targetValue = 1000f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 4000,
                    easing = FastOutSlowInEasing
                ),
                repeatMode = RepeatMode.Reverse
            )
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

        Modifier.border(1.4.dp, borderBrush, shape)
    } else {
        Modifier.border(
            1.dp,
            cs.outline.copy(alpha = 0.35f),
            shape
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .then(borderModifier)
            .clip(shape)
    ) {
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            corner = 20.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = cs.onSurface
                )

                Spacer(Modifier.height(2.dp))

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = cs.onSurface.copy(alpha = 0.7f)
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isJoined) {
                        JoinedChip()
                    } else {
                        PrimaryGradientButton(
                            text = actionText,
                            modifier = Modifier
                                .height(40.dp)
                                .widthIn(min = 90.dp),
                            onClick = onAction
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun JoinedChip() {
    val cs = MaterialTheme.colorScheme
    val dark = cs.background.luminance() < 0.5f

    // такой же стиль градиента, но слабее — secondary
    val baseColors = if (dark) {
        listOf(Violet600, Violet300.copy(alpha = 0.9f), Violet600)
    } else {
        listOf(Violet600, Violet600.copy(alpha = 0.7f), Violet600)
    }

    val gradient = Brush.horizontalGradient(
        baseColors.map { it.copy(alpha = 0.40f) }
    )

    Box(
        modifier = Modifier
            .height(40.dp)
            .widthIn(min = 90.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(gradient),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Joined",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White.copy(alpha = 0.95f)
        )
    }
}
