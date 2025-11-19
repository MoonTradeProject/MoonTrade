package com.example.moontrade.ui.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.moontrade.BuildConfig
import com.example.moontrade.R
import com.example.moontrade.model.LeaderboardEntry
import com.example.moontrade.ui.screens.components.glasskit.AvatarWithRing
import com.example.moontrade.ui.screens.components.glasskit.GlassCard
import com.example.moontrade.ui.theme.extended
import com.example.moontrade.utils.formatPercent

@Composable
fun PlayerCard(
    entry: LeaderboardEntry,
    medal: String? = null,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val ex = MaterialTheme.extended

    //
    val avatarUrl = if (entry.avatar_url?.startsWith("/") == true) {
        BuildConfig.BASE_URL + entry.avatar_url
    } else {
        entry.avatar_url
    }

    val roi = entry.roi
    val roiText = formatPercent(roi, keepPlus = true, decimals = 2)

    val displayRoiText = when {
        roi > 10_000.0  -> "> 10,000%"
        roi < -10_000.0 -> "< -10,000%"
        else            -> roiText
    }

    val roiColor = when {
        roi > 0.0  -> ex.assetChangePositive
        roi < 0.0  -> ex.assetChangeNegative
        else       -> cs.onSurfaceVariant
    }

    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 96.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (medal != null) {
                Text(
                    text = medal,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.width(12.dp))
            } else {
                Text(
                    text = "#${entry.rank}",
                    style = MaterialTheme.typography.titleSmall,
                    color = cs.onSurfaceVariant
                )
                Spacer(Modifier.width(12.dp))
            }

            AvatarWithRing(size = 48.dp) {
                AsyncImage(
                    model = avatarUrl ?: R.drawable.avatar_0,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = entry.username,
                    style = MaterialTheme.typography.titleMedium
                )

                entry.description
                    ?.takeIf { it.isNotBlank() }
                    ?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall,
                            color = cs.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "ROI",
                    style = MaterialTheme.typography.labelSmall,
                    color = cs.onSurfaceVariant
                )
                Text(
                    text = displayRoiText,
                    style = MaterialTheme.typography.titleSmall,
                    color = roiColor
                )
            }
        }
    }
}
