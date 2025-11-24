package com.example.moontrade.ui.screens.components.glasskit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.moontrade.ui.screens.main_screens.home_sub_screens.UserAssetUi
import com.example.moontrade.ui.theme.*
import com.example.moontrade.ui.theme.extended
import com.example.moontrade.utils.formatCryptoAmount
import com.example.moontrade.utils.formatFiat
import com.example.moontrade.utils.formatPercent

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
    val shape = RoundedCornerShape(corner)

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
fun ActiveStatusPill(
    modifier: Modifier = Modifier,
    label: String = "ACTIVE"
) {
    val cs = MaterialTheme.colorScheme
    val dark = isDarkTheme()
    val shape = RoundedCornerShape(40.dp)

    val backgroundBrush = if (dark) {
        Brush.horizontalGradient(listOf(Violet600,Violet300.copy(alpha = 0.8f),Violet600.copy(alpha = 1.6f)))
    } else {
        Brush.horizontalGradient(listOf(Violet600,Violet600.copy(alpha = 0.5f),Violet600.copy(alpha = 1.6f)))
    }

    val borderColor = if (dark) {
        Color.White.copy(alpha = 0.22f)
    } else {
        Color.Transparent
    }

    val textColor = if (dark) {
        cs.onPrimary
    } else {
        Color.White
    }

    Box(
        modifier = modifier
            .height(28.dp)
            .border(1.dp, borderColor, shape)
            .background(backgroundBrush, shape)
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(textColor)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = label,
                color = textColor,
                style = MaterialTheme.typography.labelMedium
            )
        }
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

@Composable
fun UserAssetCard(
    asset: UserAssetUi,
    modifier: Modifier = Modifier
) {
    val ex = MaterialTheme.extended
    val cs = MaterialTheme.colorScheme

    val change = asset.change
    val changeText = change?.let { formatPercent(it, keepPlus = true, decimals = 2) } ?: ""

    val changeColor = when {
        change == null -> cs.onSurfaceVariant
        change > 0.0   -> ex.assetChangePositive
        change < 0.0   -> ex.assetChangeNegative
        else           -> cs.onSurfaceVariant
    }

    GlassCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarWithRing {
                Text(
                    text = asset.name.first().uppercaseChar().toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = cs.onSurface
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = asset.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Amount: ${formatCryptoAmount(asset.amount)}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "≈ ${formatFiat(asset.assetValue)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (change != null) {
                Text(
                    text = changeText,
                    color = changeColor,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}
