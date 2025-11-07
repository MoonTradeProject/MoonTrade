package com.example.moontrade.ui.screens.main_screens.home_sub_screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyListScope
import com.example.moontrade.model.LeaderboardEntry
import com.example.moontrade.ui.screens.components.PlayerCard

fun LazyListScope.TopTradersSection(
    top: List<LeaderboardEntry>,
    onClickPlayer: (LeaderboardEntry) -> Unit
) {
    if (top.isEmpty()) return

    item { Text("Top Traders", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground) }
    items(top.size) { index ->
        val entry = top[index]
        val medal = when (index) { 0 -> "🥇"; 1 -> "🥈"; 2 -> "🥉"; else -> null }
        PlayerCard(entry = entry, medal = medal) { onClickPlayer(entry) }
    }
    item { Spacer(Modifier.height(8.dp)) }
}
