package com.example.moontrade.ui.screens.main_screens.home_sub_screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
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

data class UserAssetUi(
    val name: String,
    val amount: BigDecimal,
    val change: Double? = null
)

/**
 * Комбинированный вариант: вызывается внутри LazyColumn как @Composable.
 */
@Composable
fun AssetsSection(assets: List<UserAssetUi>) {
    val cs = MaterialTheme.colorScheme

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "Your Assets",
            style = MaterialTheme.typography.titleLarge,
            color = cs.onBackground
        )

        if (assets.isEmpty()) {
            Text("No assets or failed to load", color = cs.onSurface.copy(alpha = .6f))
        } else {
            assets.forEach { a ->
                AssetRow(label = a.name, value = a.amount.toPlainString(), change = a.change)
            }
        }
    }
}

@Composable
private fun AssetRow(label: String, value: String, change: Double?) {
    val cs = MaterialTheme.colorScheme
    val ex = MaterialTheme.extended

    GlassCard {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
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
                        text = label.firstOrNull()?.toString() ?: "?",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(label, style = MaterialTheme.typography.bodyLarge, color = cs.onSurface)
                    Text(
                        value,
                        style = MaterialTheme.typography.bodyMedium,
                        color = cs.onSurface.copy(alpha = .7f)
                    )
                }
            }

            if (change != null) {
                val color = if (change >= 0) ex.success else ex.danger
                val sign = if (change >= 0) "+" else ""
                Text(
                    "$sign${String.format(java.util.Locale.US, "%.2f", change)}%",
                    color = color,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
