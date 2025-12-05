package com.example.moontrade.ui.screens.main_screens.home_sections

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moontrade.R
import com.example.moontrade.model.LeaderboardEntry
import com.example.moontrade.ui.screens.components.glasskit.PlayerCard

fun LazyListScope.TopTradersSection(
    top: List<LeaderboardEntry>,
    onClickPlayer: (LeaderboardEntry) -> Unit
) {
    if (top.isEmpty()) return

    item {
        Text(
            "Top Traders",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }

    items(top.size) { index ->
        val entry = top[index]

        val medalIconRes = when (index) {
            0 -> R.drawable.ic_place_1
            1 -> R.drawable.ic_place_2
            2 -> R.drawable.ic_place_3
            3 -> R.drawable.ic_place_4
            4 -> R.drawable.ic_place_5
            else -> null
        }
        PlayerCard(
            entry = entry,
            medalIconRes = medalIconRes,
            modifier = Modifier,
            onClick = { onClickPlayer(entry) }
        )
    }
    item { Spacer(Modifier.height(8.dp)) }
}
