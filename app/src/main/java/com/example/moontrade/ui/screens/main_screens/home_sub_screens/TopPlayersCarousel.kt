package com.example.moontrade.ui.screens.main_screens.home_sub_screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moontrade.model.LeaderboardEntry
import com.example.moontrade.ui.screens.components.PlayerCard

@Composable
fun TopPlayersCarousel(
    entries: List<LeaderboardEntry>,
    onClickPlayer: (LeaderboardEntry) -> Unit
) {
    if (entries.isEmpty()) return

    val cs = MaterialTheme.colorScheme

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        //
        Text(
            text = "TOP PLAYERS",
            color = cs.onSurface.copy(alpha = .65f),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // carousel
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val top5 = entries.take(5)

            itemsIndexed(top5) { index, entry ->
                val medal = when (index) {
                    0 -> "🥇"
                    1 -> "🥈"
                    2 -> "🥉"
                    else -> null
                }

                PlayerCard(
                    entry = entry,
                    medal = medal,
                    modifier = Modifier
                        .width(320.dp)
                ) {
                    onClickPlayer(entry)
                }
            }
        }
    }
}
