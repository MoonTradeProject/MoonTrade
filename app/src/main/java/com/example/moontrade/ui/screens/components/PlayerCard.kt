package com.example.moontrade.ui.screens.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
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
    @DrawableRes medalIconRes: Int? = null,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val ex = MaterialTheme.extended

    val avatarUrl = if (entry.avatar_url?.startsWith("/") == true) {
        BuildConfig.BASE_URL + entry.avatar_url
    } else entry.avatar_url

    val roi = entry.roi
    val roiText = formatPercent(roi, keepPlus = true, decimals = 1)
    val displayRoiText = when {
        roi > 10_000.0 -> "> 10,000%"
        roi < -10_000.0 -> "< -10,000%"
        else -> roiText
    }

    val roiColor = when {
        roi > 0.0 -> ex.assetChangePositive
        roi < 0.0 -> ex.assetChangeNegative
        else -> cs.onSurfaceVariant
    }

    GlassCard(
        modifier = modifier
            .width(180.dp)
            .heightIn(min = 210.dp)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (medalIconRes != null) {
                    Box(
                        modifier = Modifier.size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(medalIconRes),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

                Text(
                    text = "TOP ${entry.rank}",
                    style = MaterialTheme.typography.titleSmall,
                    color = cs.onSurface,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Clip
                )

                Text(
                    text = displayRoiText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = roiColor,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Clip
                )
            }

            // аватар
            AvatarWithRing(size = 48.dp) {
                AsyncImage(
                    model = avatarUrl ?: R.drawable.avatar_0,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // ник
            Text(
                text = entry.username,
                style = MaterialTheme.typography.bodyMedium,
                color = cs.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.widthIn(max = 120.dp)
            )
        }
    }
}
