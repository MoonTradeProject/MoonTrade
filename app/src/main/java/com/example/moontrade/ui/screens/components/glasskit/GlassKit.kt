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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.moontrade.ui.screens.main_screens.home_sub_screens.UserAssetUi
import com.example.moontrade.ui.theme.Violet200
import com.example.moontrade.ui.theme.Violet300
import com.example.moontrade.ui.theme.Violet600
import com.example.moontrade.ui.theme.extended
import com.example.moontrade.utils.formatCryptoAmount
import com.example.moontrade.utils.formatFiat
import com.example.moontrade.utils.formatPercent


@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    corner: Dp = 22.dp,
    overlay: Brush? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val ex = MaterialTheme.extended
    val shape = RoundedCornerShape(corner)

    Box(
        modifier = modifier
            .clip(shape)
            .background(
                Brush.verticalGradient(
                    listOf(
                        Violet300.copy(alpha = 0.22f),   // сверху дымка
                        ex.glassCard.copy(alpha = 0.020f),  // акцент на середину
                        Violet600.copy(alpha = 0.22f)    // внизу подсветка
                    )
                )
            )
            .border(
                width = 0.25.dp,
                brush = Brush.linearGradient(
                    listOf(
                        Violet200.copy(alpha = 0.25f),  // подсветка сверху
                        Violet200.copy(alpha = 0.24f)  // затухает внизу
                    )
                ),
                shape = shape
            )
            .padding(16.dp)
    ) {

        // OPTIONAL overlay
        if (overlay != null) {
            Box(
                Modifier
                    .matchParentSize()
                    .graphicsLayer { alpha = 0.20f }
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
    val isDark = androidx.compose.foundation.isSystemInDarkTheme()
    val shape = RoundedCornerShape(999.dp)

    val backgroundBrush = if (isDark) {

        Brush.horizontalGradient(
            listOf(
                Color(0xFF6F4BFF),
                Color(0xFF9D73FF)
            )
        )
    } else {

        Brush.horizontalGradient(
            listOf(
                Color(0xFFFDFBFF),
                Color(0xFFE4D6FF)
            )
        )
    }

    val borderColor = if (isDark) {
        Color(0xFFD8C6FF).copy(alpha = 0.8f)
    } else {
        Color(0xFFE0D3FF)
    }

    val textColor = if (isDark) {
        Color.White
    } else {
        Color(0xFF4B3A7A)
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
/** карточка ассета */
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
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = asset.name,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "Amount: ${formatCryptoAmount(asset.amount)}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "≈ ${formatFiat(asset.assetValue)}",
                    style = MaterialTheme.typography.bodySmall
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
