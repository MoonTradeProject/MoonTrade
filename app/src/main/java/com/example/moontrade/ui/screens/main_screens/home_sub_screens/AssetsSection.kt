package com.example.moontrade.ui.screens.main_screens.home_sub_screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.unit.dp
import com.example.moontrade.ui.screens.components.glasskit.UserAssetCard
import com.example.moontrade.ui.theme.Violet200
import com.example.moontrade.ui.theme.extended
import com.example.moontrade.utils.formatFiat
import java.math.BigDecimal

data class UserAssetUi(
    val name: String,
    val amount: BigDecimal,
    val assetValue: BigDecimal,
    val change: Double? = null
)
@Composable
fun AssetsSection(
    assets: List<UserAssetUi>,
    isLoading: Boolean,
    error: String?
) {
    val cs = MaterialTheme.colorScheme

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

        when {
            isLoading -> {
                Text(
                    text = "Loading…",
                    color = cs.onSurface.copy(alpha = .6f)
                )
            }
            error != null -> {
                Text(
                    text = "Failed to load: $error",
                    color = cs.error
                )
            }
            assets.isEmpty() -> {
                Text(
                    text = "No assets available",
                    color = cs.onSurface.copy(alpha = .6f)
                )
            }
            else -> {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    assets.forEach { a ->
                        UserAssetCard(
                            asset = a,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                val total = assets.fold(BigDecimal.ZERO) { acc, item -> acc + item.assetValue }

                Spacer(Modifier.height(16.dp))

                TotalValueCard(
                    total = total,
                    assetsCount = assets.size
                )
            }
        }
    }
}

@Composable
private fun TotalValueCard(
    total: BigDecimal,
    assetsCount: Int
) {
    val cs = MaterialTheme.colorScheme
    val ex = MaterialTheme.extended
    val isDark = isSystemInDarkTheme()

    val backgroundBrush = if (isDark) {
        Brush.verticalGradient(
            listOf(
                ex.glassCard.copy(alpha = 0.35f),
                Color.Transparent,
                ex.glassCard.copy(alpha = 0.55f)
            )
        )
    } else {
        Brush.verticalGradient(
            listOf(
                Violet200.copy(alpha = .2f),
                Color.Transparent,
                Violet200.copy(alpha = 0.2f)
            )
        )
    }
    val valueTextStyle = if (isDark) {
        MaterialTheme.typography.headlineMedium.copy(
            shadow = Shadow(
                color = ex.success.copy(alpha = 0.85f),
                offset = Offset(0f, 0f),
                blurRadius = 18f
            )
        )
    } else {
        MaterialTheme.typography.headlineMedium
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(backgroundBrush)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "TOTAL VALUE",
                    style = MaterialTheme.typography.labelLarge,
                    color = cs.onSurface.copy(
                        alpha = if (isDark) 0.75f else 0.85f
                    )
                )

                Text(
                    text = "USDT",
                    style = MaterialTheme.typography.labelMedium,
                    color = cs.onSurface.copy(
                        alpha = if (isDark) 0.6f else 0.7f
                    )
                )
            }

            Text(
                text = "$${formatFiat(total)}",
                style = valueTextStyle,
                color = ex.success
            )

            Text(
                text = "Across $assetsCount assets",
                style = MaterialTheme.typography.labelSmall,
                color = cs.onSurface.copy(alpha = 0.55f)
            )
        }
    }
}
