package com.example.moontrade.ui.screens.components.glasskit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Shape
import androidx.compose.foundation.layout.BoxScope
import com.example.moontrade.ui.theme.*
import com.example.moontrade.ui.theme.extended

@Composable
private fun isDarkTheme(): Boolean {
    val cs = MaterialTheme.colorScheme
    return cs.background.luminance() < 0.5f
}
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    corner: Dp = 22.dp,
    overlay: Brush? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val ex = MaterialTheme.extended
    val dark = isDarkTheme()
    val shape: Shape = RoundedCornerShape(corner)

    val backgroundBrush: Brush
    val borderBrush: Brush

    if (dark) {
        backgroundBrush = Brush.verticalGradient(
            listOf(
                Violet300.copy(alpha = 0.22f),
                ex.glassCard.copy(alpha = 0.020f),
                Violet600.copy(alpha = 0.22f)
            )
        )
        borderBrush = Brush.linearGradient(
            listOf(
                Violet200.copy(alpha = 0.25f),
                Violet200.copy(alpha = 0.24f)
            )
        )
    } else {
        backgroundBrush = Brush.verticalGradient(
            listOf(
                Color.White,
                Color.White
            )
        )
        borderBrush = Brush.linearGradient(
            listOf(
                Violet200.copy(alpha = 0.25f),
                Violet200.copy(alpha = 0.34f)
            )
        )
    }
    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundBrush)
            .border(
                width = if (dark) 0.25.dp else 1.8.dp,
                brush = borderBrush,
                shape = shape
            )
            .padding(16.dp)
    ) {
        if (overlay != null) {
            Box(
                Modifier
                    .matchParentSize()
                    .graphicsLayer { alpha = 0.18f }
                    .clip(shape)
                    .background(overlay)
            )
        }
        Column(content = content)
    }
}
@Composable
fun AvatarWithRing(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    ringBrush: Brush = MaterialTheme.extended.gradientAvatar,
    innerColor: Color = MaterialTheme.colorScheme.surface,
    borderPadding: Dp = 3.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .size(size)
            .background(ringBrush, CircleShape)
            .padding(borderPadding)
            .clip(CircleShape)
            .background(innerColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
