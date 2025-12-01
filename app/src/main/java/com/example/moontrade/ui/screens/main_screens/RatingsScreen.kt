package com.example.moontrade.ui.screens.main_screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.moontrade.BuildConfig
import com.example.moontrade.R
import com.example.moontrade.model.LeaderboardEntry
import com.example.moontrade.navigation.NavRoutes
import com.example.moontrade.ui.screens.components.bars.SectionHeader
import com.example.moontrade.ui.screens.components.bars.TopBar
import com.example.moontrade.ui.screens.components.glasskit.AvatarWithRing
import com.example.moontrade.ui.screens.components.glasskit.GlassCard
import com.example.moontrade.ui.theme.GreenUp
import com.example.moontrade.ui.theme.RedDown
import com.example.moontrade.ui.theme.Violet600
import com.example.moontrade.utils.formatPercent
import com.example.moontrade.viewmodels.LeaderboardViewModel
import com.example.moontrade.viewmodels.ProfileViewModel
import com.example.moontrade.viewmodels.SelectedPlayerViewModel

@Composable
fun RatingsScreen(
    navController: NavController,
    leaderboardViewModel: LeaderboardViewModel,
    selectedPlayerViewModel: SelectedPlayerViewModel,
    profileViewModel: ProfileViewModel
) {
    LaunchedEffect(Unit) { leaderboardViewModel.loadLeaderboard() }

    val entries by leaderboardViewModel.entries.collectAsState()
    val myNickname by profileViewModel.nickname.collectAsState()

    RatingsScreenContent(
        entries = entries,
        myNickname = myNickname,
        onBack = { navController.popBackStack() },
        onClickPlayer = {
            selectedPlayerViewModel.set(it)
            navController.navigate(NavRoutes.PLAYER_PROFILE)
        }
    )
}
@Composable
private fun RatingsScreenContent(
    entries: List<LeaderboardEntry>,
    myNickname: String,
    onBack: () -> Unit,
    onClickPlayer: (LeaderboardEntry) -> Unit
) {
    val players = remember(entries) {
        entries
            .filterNot { it.username == "market-bot" }
            .sortedBy { it.rank }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "Leaderboard",
                showBack = true,
                onBack = onBack
            )
        }
    ) { padding ->

        if (players.isEmpty()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No players yet")
            }
            return@Scaffold
        }

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
            itemsIndexed(players) { index, entry ->
                val displayRank = index + 1

                LeaderboardRow(
                    entry = entry,
                    rank = displayRank,
                    isMe = entry.username == myNickname,
                    onClick = { onClickPlayer(entry) }
                )
            }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
private fun LeaderboardRow(
    entry: LeaderboardEntry,
    rank: Int,
    isMe: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    val fontScale = density.fontScale
    val isCompactWidth = configuration.screenWidthDp < 360
    val useStackedLayout = fontScale >= 1.3f || isCompactWidth

    val avatarUrl =
        if (entry.avatar_url?.startsWith("/") == true) BuildConfig.BASE_URL + entry.avatar_url
        else entry.avatar_url

    val roi = entry.roi
    val roiTextRaw = formatPercent(roi, keepPlus = true, decimals = 2)
    val roiText = when {
        roi > 10_000 -> "> 10,000%"
        roi < -10_000 -> "< -10,000%"
        else -> roiTextRaw
    }

    val roiColor = when {
        roi > 0 -> GreenUp
        roi < 0 -> RedDown
        else -> cs.onSurfaceVariant
    }

    val roiStyle = when {
        fontScale >= 1.5f -> MaterialTheme.typography.bodySmall
        fontScale >= 1.2f -> MaterialTheme.typography.bodyMedium
        else -> MaterialTheme.typography.titleMedium
    }
    val nameStyle = when {
        fontScale >= 1.5f -> MaterialTheme.typography.bodyMedium
        fontScale >= 1.2f -> MaterialTheme.typography.titleSmall
        else -> MaterialTheme.typography.titleMedium
    }

    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable(onClick = onClick),
        corner = 18.dp,
        overlay = null
    ) {
        if (!useStackedLayout) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = rank.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = cs.onSurface,
                    modifier = Modifier.width(20.dp),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.width(10.dp))

                AvatarWithRing(size = 36.dp) {
                    AsyncImage(
                        model = avatarUrl ?: R.drawable.avatar_0,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    if (isMe) {
                        Text(
                            text = "You",
                            style = MaterialTheme.typography.bodySmall,
                            color = Violet600
                        )
                    }
                    Text(
                        text = entry.username,
                        style = nameStyle,
                        color = cs.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.width(12.dp))

                Text(
                    text = roiText,
                    style = roiStyle,
                    color = roiColor,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    softWrap = false,
                    modifier = Modifier.widthIn(min = 72.dp)
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = rank.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = cs.onSurface,
                        modifier = Modifier.width(20.dp),
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.width(10.dp))

                    AvatarWithRing(size = 36.dp) {
                        AsyncImage(
                            model = avatarUrl ?: R.drawable.avatar_0,
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        if (isMe) {
                            Text(
                                text = "You",
                                style = MaterialTheme.typography.bodySmall,
                                color = cs.primary
                            )
                        }
                        Text(
                            text = entry.username,
                            style = nameStyle,
                            color = cs.onSurface,
                            maxLines = 2,
                            overflow = TextOverflow.Clip
                        )
                    }
                }
                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                ) {
                    Text(
                        text = roiText,
                        style = roiStyle,
                        color = roiColor,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }
    }
}

// Preview

private fun fakeLeaderboardEntries(): List<LeaderboardEntry> {
    val names = listOf("market-bot", "GODDAMN", "TraderX", "MoonShot", "SlowBro")
    val rois = listOf(10000.0, 4444.33, 120.5, 75.2, 12.3)
    val desc = listOf(
        "AI-based trader",
        "Consistent growth",
        "Steady performer",
        "Swing trader",
        "Low-risk strategy"
    )
    return List(names.size) { i ->
        val rank = i + 1
        LeaderboardEntry(
            uid = rank.toString(),
            roi = rois[i],
            rank = rank,
            avatar_url = null,
            description = desc[i],
            tags = listOf("Top $rank"),
            achievements = listOf("${rois[i]}% ROI"),
            username = names[i]
        )
    }
}
@Preview(showBackground = true, widthDp = 380, heightDp = 800)
@Composable
fun RatingsScreen_Preview_Normal() {
    RatingsScreenContent(
        entries = fakeLeaderboardEntries(),
        myNickname = "TraderX",
        onBack = {},
        onClickPlayer = {}
    )
}

@Preview(
    showBackground = true,
    widthDp = 380,
    heightDp = 800,
    fontScale = 1.4f
)
@Composable
fun RatingsScreen_Preview_BigFont() {
    RatingsScreenContent(
        entries = fakeLeaderboardEntries(),
        myNickname = "TraderX",
        onBack = {},
        onClickPlayer = {}
    )
}
@Preview(
    showBackground = true,
    device = "spec:width=320dp,height=700dp,dpi=320"
)
@Composable
fun RatingsScreen_Preview_SmallDevice() {
    RatingsScreenContent(
        entries = fakeLeaderboardEntries(),
        myNickname = "TraderX",
        onBack = {},
        onClickPlayer = {}
    )
}
