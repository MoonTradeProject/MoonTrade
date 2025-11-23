package com.example.moontrade.ui.screens.main_screens.home_sub_screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moontrade.R
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
        Text(
            text = "TOP PLAYERS",
            color = cs.onSurface.copy(alpha = .65f),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val top5 = entries.take(5)

            itemsIndexed(top5) { index, entry ->
                val medalIconRes = when (index) {
                    0 -> R.drawable.ic_place_1  // 1 место
                    1 -> R.drawable.ic_place_2  // 2 место
                    2 -> R.drawable.ic_place_3  // 3 место
                    3 -> R.drawable.ic_place_4  // 4 место (квадрат с "4")
                    4 -> R.drawable.ic_place_5  // НОВЫЙ 64x64 с "5"
                    else -> null
                }

                PlayerCard(
                    entry = entry,
                    medalIconRes = medalIconRes,
                    modifier = Modifier.width(180.dp),
                    onClick = { onClickPlayer(entry) }
                )
            }
        }
    }
}
