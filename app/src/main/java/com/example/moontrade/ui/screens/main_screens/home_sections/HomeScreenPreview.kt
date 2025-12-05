package com.example.moontrade.ui.screens.main_screens.home_sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moontrade.model.Mode
import com.example.moontrade.model.LeaderboardEntry
import com.example.moontrade.ui.screens.components.bars.SectionHeader
import com.example.moontrade.ui.screens.main_screens.HomeScreenContent
import java.math.BigDecimal

private fun fakeLeaderboardEntries(): List<LeaderboardEntry> {
    val names = listOf("market-bot", "GODDAMN", "TraderX", "MoonShot", "SlowBro")
    val rois = listOf(10000.0, 4444.33, 120.5, 75.2, 12.3)
    val descriptions = listOf(
        "AI-based trader",
        "Consistent growth",
        "Steady performer",
        "Swing trader",
        "Low-risk strategy"
    )
    val achievements = listOf("10k% ROI", "4400% ROI", "120% ROI", "75% ROI", "12% ROI")

    return names.indices.map { i ->
        LeaderboardEntry(
            uid = (i + 1).toString(),
            username = names[i],
            roi = rois[i],
            rank = i + 1,
            avatar_url = null,
            description = descriptions[i],
            tags = listOf("Top ${i + 1}"),
            achievements = listOf(achievements[i])
        )
    }
}

@Composable
private fun HomeScreenPreviewContent() {
    val modes = listOf(
        SelectableMode(Mode.Main, "Main"),
        SelectableMode(Mode.Tournament("1"), "BTC Cup")
    )
    var selected by remember { mutableStateOf(modes.first()) }

    val assets = listOf(
        UserAssetUi(
            name = "ADAUSDT",
            amount = BigDecimal("19.0"),
            assetValue = BigDecimal("7.81"),
            change = -2.5
        ),
        UserAssetUi(
            name = "SOLUSDT",
            amount = BigDecimal("2.0"),
            assetValue = BigDecimal("269.18"),
            change = -2.5
        ),
    )

    val leaders = fakeLeaderboardEntries()

    MaterialTheme {
        HomeScreenContent(
            nickname = "TraderX",
            selectedTags = listOf("Sniper", "Top 10"),
            avatarId = 0,
            avatarUrl = null,
            balanceText = "$1,940.85",
            roiValue = -3.0,
            selectableModes = modes,
            selectedMode = selected,
            onSelectMode = { },
            assets = assets,
            isAssetsLoading = false,
            assetsError = null,
            leaderboardEntries = leaders,
            onOpenSettings = {},
            onOpenOrders = {},
            onOpenPlayer = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 380, heightDp = 800)
@Composable
fun HomeScreen_Preview_Normal() {
    HomeScreenPreviewContent()
}
@Preview(
    showBackground = true,
    widthDp = 380,
    heightDp = 800,
    fontScale = 1.4f
)
@Composable
fun HomeScreen_Preview_BigFont() {
    HomeScreenPreviewContent()
}

@Preview(
    showBackground = true,
    device = "spec:width=320dp,height=700dp,dpi=320"
)
@Composable
fun HomeScreen_Preview_SmallDevice() {
    HomeScreenPreviewContent()
}

@Preview(showBackground = true, widthDp = 380, heightDp = 200)
@Composable
fun TradingModeSection_Preview() {
    val modes = listOf(
        SelectableMode(Mode.Main, "Regular trading"),
        SelectableMode(Mode.Tournament("1"), "Weekly Challenge"),
        SelectableMode(Mode.Tournament("2"), "Two Weeks Challenge")
    )
    var selected by remember { mutableStateOf(modes.first()) }

    MaterialTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SectionHeader("Trading mode")

            ModeSelector(
                selected = selected,
                items = modes,
                onSelect = { selected = it },
                modifier = Modifier.fillMaxWidth()
            )

            val modeDescription = when (selected.mode) {
                is Mode.Main -> "Regular trading without a tournament"
                is Mode.Tournament -> "You are trading inside: ${selected.label}"
            }

            Text(
                text = modeDescription,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
