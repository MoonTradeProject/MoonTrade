package com.example.moontrade.ui.screens.main_screens.home_sub_screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyListScope
import com.example.moontrade.ui.screens.components.glasskit.GlassCard
import java.math.BigDecimal

data class UserAssetUi(val name: String, val amount: BigDecimal)

fun LazyListScope.AssetsSection(assets: List<UserAssetUi>) {
    item { Text("Your Assets", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground) }
    if (assets.isEmpty()) {
        item { Text("No assets or failed to load", color = MaterialTheme.colorScheme.onSurface.copy(.6f)) }
    } else {
        items(assets.size) { i ->
            val a = assets[i]
            AssetRow(label = a.name, value = a.amount.toPlainString())
        }
    }
}

@Composable
private fun AssetRow(label: String, value: String) {
    val cs = MaterialTheme.colorScheme
    GlassCard {
        Row(Modifier.fillMaxWidth()) {
            Column(Modifier.weight(1f)) {
                Text(label, style = MaterialTheme.typography.bodyLarge, color = cs.onSurface)
                Text(value, style = MaterialTheme.typography.bodyMedium, color = cs.onSurface.copy(alpha = .75f))
            }
        }
    }
}
