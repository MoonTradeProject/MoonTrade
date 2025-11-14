package com.example.moontrade.ui.screens.main_screens.home_sub_screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.moontrade.ui.screens.components.glasskit.GlassCard
import com.example.moontrade.ui.theme.extended
import java.math.BigDecimal
import com.example.moontrade.utils.formatCryptoAmount
import com.example.moontrade.utils.formatFiat
import com.example.moontrade.utils.formatPercent

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

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Your Assets",
            style = MaterialTheme.typography.titleLarge,
            color = cs.onBackground
        )

        when {
            isLoading -> Text("Loading…", color = cs.onSurface.copy(alpha = .6f))
            error != null -> Text("Failed to load: $error", color = cs.error)
            assets.isEmpty() -> Text("No assets available", color = cs.onSurface.copy(alpha = .6f))
            else -> {
                assets.forEach { a ->
                    AssetRow(
                        label = a.name,
                        amountText = formatCryptoAmount(a.amount),
                        assetValueUsdText = formatFiat(a.assetValue),
                        changeText = a.change?.let { formatPercent(it, keepPlus = true, decimals = 2) }
                    )
                }

                val total = assets.fold(BigDecimal.ZERO) { acc, item -> acc + item.assetValue }

                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Total value: $${formatFiat(total)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = cs.onSurface
                )
            }
        }
    }
}

@Composable
private fun AssetRow(
    label: String,
    amountText: String,
    assetValueUsdText: String,
    changeText: String?
) {
    val cs = MaterialTheme.colorScheme
    val ex = MaterialTheme.extended

    // tabular figures for even columns
    @Suppress("SpellCheckingInspection")
    val digitsStyle = MaterialTheme.typography.bodyMedium.copy(
        fontFeatureSettings = "tnum"
    )

    GlassCard {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // avatar (fixed)
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(ex.gradientAccent),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label.firstOrNull()?.uppercase() ?: "?",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
            }

            Spacer(Modifier.width(12.dp))

            // the central column is stretched and does not push the percentages
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    label,
                    style = MaterialTheme.typography.bodyLarge,
                    color = cs.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Amount: $amountText",
                    style = digitsStyle,
                    color = cs.onSurface.copy(alpha = .7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "≈ $$assetValueUsdText",
                    style = digitsStyle,
                    color = cs.onSurface.copy(alpha = 0.85f),
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
            }

            // the right column is fixed width and does not jump
            if (changeText != null) {
                val isPlus = changeText.startsWith("+")
                val color = if (isPlus) ex.success else ex.danger
                Text(
                    text = changeText,
                    color = color,
                    style = digitsStyle,
                    modifier = Modifier.widthIn(min = 72.dp), // can be increased to 72-80dp if desired
                    textAlign = TextAlign.End,
                    maxLines = 1
                )
            }
        }
    }
}
