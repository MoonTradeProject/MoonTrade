package com.example.moontrade.ui.screens.main_screens.home_sub_screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moontrade.ui.screens.components.glasskit.UserAssetCard
import java.math.BigDecimal
import com.example.moontrade.utils.formatFiat

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
            text = "YOUR ASSETS",
            color = cs.onSurface.copy(alpha = .65f),
            style = MaterialTheme.typography.labelLarge
        )

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
                // список карточек ассетов
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    assets.forEach { a ->
                        UserAssetCard(
                            asset = a,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
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
