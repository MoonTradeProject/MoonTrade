package com.example.moontrade.ui.screens.main_screens.home_sections

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moontrade.R
import com.example.moontrade.model.LeaderboardEntry
import com.example.moontrade.ui.screens.components.glasskit.PlayerCard

@Composable
fun TopPlayersCarousel(
    entries: List<LeaderboardEntry>,
    onClickPlayer: (LeaderboardEntry) -> Unit
) {
    if (entries.isEmpty()) return

    val top5 = entries
        .filterNot { it.username == "market-bot" }
        .take(5)

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 0.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(top5) { index, entry ->
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
        }
    }
}
