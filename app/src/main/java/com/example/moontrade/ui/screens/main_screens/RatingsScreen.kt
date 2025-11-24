package com.example.moontrade.ui.screens.main_screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.moontrade.BuildConfig
import com.example.moontrade.R
import com.example.moontrade.model.LeaderboardEntry
import com.example.moontrade.navigation.NavRoutes
import com.example.moontrade.ui.screens.components.bars.TopBar
import com.example.moontrade.ui.screens.components.glasskit.AvatarWithRing
import com.example.moontrade.ui.theme.extended
import com.example.moontrade.utils.formatPercent
import com.example.moontrade.viewmodels.LeaderboardViewModel
import com.example.moontrade.viewmodels.SelectedPlayerViewModel
import com.example.moontrade.viewmodels.ProfileViewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.moontrade.ui.screens.components.bars.SectionHeader

@Composable
fun RatingsScreen(
    navController: NavController,
    leaderboardViewModel: LeaderboardViewModel,
    selectedPlayerViewModel: SelectedPlayerViewModel,
    profileViewModel: ProfileViewModel
) {
    LaunchedEffect(Unit) {
        leaderboardViewModel.loadLeaderboard()
    }

    val entries: List<LeaderboardEntry> by leaderboardViewModel
        .entries
        .collectAsState()
    val myNickname by profileViewModel.nickname.collectAsState()
    val sortedEntries = remember(entries) {
        entries.sortedBy { it.rank }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "Leaderboard",
                showBack = true,
                onBack = { navController.popBackStack() }
            )
        }
    ) { padding ->
        if (sortedEntries.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No players yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                item {
                    SectionHeader(
                        "Top Players",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                    )
                }

                items(sortedEntries) { entry ->
                    val isMe = entry.username == myNickname

                    LeaderboardRow(
                        entry = entry,
                        isMe = isMe,
                        onClick = {
                            selectedPlayerViewModel.set(entry)
                            navController.navigate(NavRoutes.PLAYER_PROFILE)
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
@Composable
private fun LeaderboardRow(
    entry: LeaderboardEntry,
    isMe: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val ex = MaterialTheme.extended

    val avatarUrl = if (entry.avatar_url?.startsWith("/") == true) {
        BuildConfig.BASE_URL + entry.avatar_url
    } else entry.avatar_url

    val roi = entry.roi
    val roiTextRaw = formatPercent(roi, keepPlus = true, decimals = 2)
    val roiText = when {
        roi > 10_000.0 -> "> 10,000%"
        roi < -10_000.0 -> "< -10,000%"
        else -> roiTextRaw
    }

    val roiColor = when {
        roi > 0.0 -> ex.assetChangePositive
        roi < 0.0 -> ex.assetChangeNegative
        else -> cs.onSurfaceVariant
    }
    val isTop3 = entry.rank in 1..3

    val baseBg = cs.surfaceVariant.copy(alpha = 0.12f)
    val topBg = cs.primary.copy(alpha = 0.16f)
    val meBg = cs.primary.copy(alpha = 0.28f)

    val bgColor by animateColorAsState(
        when {
            isMe -> meBg
            isTop3 -> topBg
            else -> baseBg
        },
        label = "rowBg"
    )
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick),
        color = bgColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = entry.rank.toString(),
                style = MaterialTheme.typography.titleMedium,
                color = cs.onSurface,
                modifier = Modifier.width(32.dp),
                textAlign = TextAlign.Center
            )

            AvatarWithRing(size = 36.dp) {
                AsyncImage(
                    model = avatarUrl ?: R.drawable.avatar_0,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                if (isMe) {
                    Text(
                        text = "You",
                        style = MaterialTheme.typography.bodySmall,
                        color = cs.primary,
                    )
                }
                Text(
                    text = entry.username,
                    style = MaterialTheme.typography.titleMedium,
                    color = cs.onSurface,
                    maxLines = 1
                )
            }

            Text(
                text = roiText,
                style = MaterialTheme.typography.titleMedium,
                color = roiColor,
                textAlign = TextAlign.End,
                modifier = Modifier.widthIn(min = 80.dp)
            )
        }
    }
}
