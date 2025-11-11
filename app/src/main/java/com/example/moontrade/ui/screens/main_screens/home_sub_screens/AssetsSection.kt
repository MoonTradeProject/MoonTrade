package com.example.moontrade.ui.screens.main_screens.home_sub_screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.moontrade.ui.screens.components.glasskit.GlassCard
import com.example.moontrade.ui.theme.extended
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Locale

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
            isLoading -> {
                Text("Loading…", color = cs.onSurface.copy(alpha = .6f))
            }
            error != null -> {
                Text("Failed to load: $error", color = cs.error)
            }
            assets.isEmpty() -> {
                Text("No assets available", color = cs.onSurface.copy(alpha = .6f))
            }
            else -> {
                assets.forEach { a ->
                    AssetRow(
                        label = a.name,
                        value = a.amount.toPlainString(),
                        assetValueUsd = a.assetValue
                            .setScale(2, RoundingMode.HALF_UP)
                            .toPlainString(),
                        change = a.change
                    )
                }

                val total = assets.fold(BigDecimal.ZERO) { acc, asset -> acc + asset.assetValue }

                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Total value: $${total.setScale(2, RoundingMode.HALF_UP)}",
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
    value: String,
    assetValueUsd: String,
    change: Double?
) {
    val cs = MaterialTheme.colorScheme
    val ex = MaterialTheme.extended

    GlassCard {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
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
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(
                        label,
                        style = MaterialTheme.typography.bodyLarge,
                        color = cs.onSurface
                    )
                    Text(
                        text = "Amount: $value",
                        style = MaterialTheme.typography.bodyMedium,
                        color = cs.onSurface.copy(alpha = .7f)
                    )
                    Text(
                        text = "≈ $$assetValueUsd",
                        style = MaterialTheme.typography.bodyMedium,
                        color = cs.primary
                    )
                }
            }

            if (change != null) {
                val color = if (change >= 0) ex.success else ex.danger
                val sign = if (change >= 0) "+" else ""
                Text(
                    "$sign${String.format(Locale.US, "%.2f", change)}%",
                    color = color,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
