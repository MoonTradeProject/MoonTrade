package com.example.moontrade.ui.screens.main_screens.home_sections

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import com.example.moontrade.ui.screens.components.glasskit.AvatarWithRing
import com.example.moontrade.ui.screens.components.glasskit.GlassCard
import com.example.moontrade.ui.theme.extended
import com.example.moontrade.utils.formatCryptoAmount
import com.example.moontrade.utils.formatFiat
import com.example.moontrade.utils.formatPercent

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
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = asset.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Amount: ${formatCryptoAmount(asset.amount)}",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "≈ ${formatFiat(asset.assetValue)}",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.width(8.dp))

            if (change != null) {
                Text(
                    text = changeText,
                    color = changeColor,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Clip
                )
            }
        }
    }
}
