package com.example.moontrade.ui.screens.main_screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moontrade.R
import com.example.moontrade.model.Mode
import com.example.moontrade.model.LeaderboardEntry
import com.example.moontrade.navigation.NavRoutes
import com.example.moontrade.ui.screens.components.bars.TopBar
import com.example.moontrade.ui.screens.components.bars.SectionHeader
import com.example.moontrade.ui.screens.main_screens.home_sub_screens.*
import com.example.moontrade.ui.screens.main_screens.order_sub_screen.MyOrdersCard
import com.example.moontrade.viewmodels.*
import java.math.BigDecimal
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    balanceViewModel: BalanceViewModel,
    tournamentsViewModel: TournamentsViewModel,
    profileViewModel: ProfileViewModel,
    userAssetsViewModel: UserAssetsViewModel,
    leaderboardViewModel: LeaderboardViewModel,
    selectedPlayerViewModel: SelectedPlayerViewModel
) {
    val nickname by profileViewModel.nickname.collectAsState()
    val selectedTags by profileViewModel.selectedTags.collectAsState()
    val avatarId by profileViewModel.avatarId.collectAsState()
    val avatarUrl by profileViewModel.avatarUrl.collectAsState()

    val roi by balanceViewModel.roi.collectAsState()
    val currentMode by balanceViewModel.mode.collectAsState()
    val balance by balanceViewModel.balance.collectAsState()

    val tournaments by tournamentsViewModel.tournaments.collectAsState()
    val userAssets by userAssetsViewModel.assets.collectAsState()
    val leaderboardEntries by leaderboardViewModel.entries.collectAsState()

    val isLoading by userAssetsViewModel.loading.collectAsState()
    val assetsError by userAssetsViewModel.error.collectAsState()

    val selectableModes = remember(tournaments) {
        listOf(SelectableMode(Mode.Main, "Regular trading")) +
                tournaments.filter { it.isJoined }
                    .map { SelectableMode(Mode.Tournament(it.id.toString()), it.name) }
    }

    var selected by remember(currentMode, selectableModes) {
        mutableStateOf(selectableModes.find { it.mode == currentMode } ?: selectableModes.first())
    }

    LaunchedEffect(Unit) { balanceViewModel.connect() }
    LaunchedEffect(Unit) { userAssetsViewModel.loadUserAssets() }
    LaunchedEffect(Unit) { leaderboardViewModel.loadLeaderboard() }

    LaunchedEffect(selected.mode) {
        balanceViewModel.changeMode(selected.mode)
        userAssetsViewModel.loadUserAssets()
    }

    val roiValue = roi
        .replace("%", "")
        .replace(",", ".")
        .toDoubleOrNull() ?: 0.0

    val roiLabel = (if (roiValue >= 0) "+" else "") +
            String.format(Locale.US, "%.1f%%", roiValue)

    val assetsUi = userAssets.map { a ->
        UserAssetUi(
            name = a.asset_name,
            amount = a.amount,
            assetValue = a.asset_value,
            change = listOf(-2.5, 1.2, 0.8, -0.6).random()
        )
    }

    HomeScreenContent(
        nickname = nickname,
        selectedTags = selectedTags,
        avatarId = avatarId,
        avatarUrl = avatarUrl,
        balanceText = balance,
        roiValue = roiValue,
        roiLabel = roiLabel,
        selectableModes = selectableModes,
        selectedMode = selected,
        onSelectMode = { selected = it },
        assets = assetsUi,
        isAssetsLoading = isLoading,
        assetsError = assetsError,
        leaderboardEntries = leaderboardEntries,
        onOpenSettings = { navController.navigate(NavRoutes.SETTINGS) },
        onOpenOrders = { navController.navigate(NavRoutes.USER_ORDERS) },
        onOpenPlayer = { entry ->
            selectedPlayerViewModel.set(entry)
            navController.navigate(NavRoutes.PLAYER_PROFILE)
        }
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    nickname: String,
    selectedTags: List<String>,
    avatarId: Int,
    avatarUrl: String?,
    balanceText: String,
    roiValue: Double,
    roiLabel: String,
    selectableModes: List<SelectableMode>,
    selectedMode: SelectableMode,
    onSelectMode: (SelectableMode) -> Unit,
    assets: List<UserAssetUi>,
    isAssetsLoading: Boolean,
    assetsError: String?,
    leaderboardEntries: List<LeaderboardEntry>,
    onOpenSettings: () -> Unit,
    onOpenOrders: () -> Unit,
    onOpenPlayer: (LeaderboardEntry) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopBar(
                title = null,
                showBack = false,
                navigationContent = {},
                actions = {
                    IconButton(onClick = onOpenSettings) {
                        Image(
                            painter = painterResource(R.drawable.ic_settings),
                            contentDescription = "Settings",
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            item {
                SectionHeader("Trading mode")
                Spacer(Modifier.height(8.dp))

                ModeSelector(
                    selected = selectedMode,
                    items = selectableModes,
                    onSelect = onSelectMode,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(6.dp))

                val modeDescription = when (selectedMode.mode) {
                    is Mode.Main -> "Regular trading without a tournament"
                    is Mode.Tournament -> "You are trading inside: ${selectedMode.label} mode"
                }

                Text(
                    text = modeDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            item {
                ProfileSummaryCard(
                    nickname = nickname,
                    selectedTags = selectedTags,
                    avatarId = avatarId,
                    avatarUrl = avatarUrl,
                    balanceText = balanceText,
                    roiValue = roiValue,
                    roiLabel = roiLabel,
                    avatarResIdFrom = ::resolveAvatarRes
                )
            }
            item {
                SectionHeader("Your assets")
                AssetsSection(
                    assets = assets,
                    isLoading = isAssetsLoading,
                    error = assetsError
                )
            }
            item {
                MyOrdersCard(
                    onClick = onOpenOrders
                )
            }
            item {
                SectionHeader("Top players")
                TopPlayersCarousel(
                    entries = leaderboardEntries,
                    onClickPlayer = onOpenPlayer
                )
            }
        }
    }
}

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
// ПРЕВЬЮ

@OptIn(ExperimentalMaterial3Api::class)
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
            roiLabel = "-3.0%",
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

// обычное превью
@Preview(showBackground = true, widthDp = 380, heightDp = 800)
@Composable
fun HomeScreen_Preview_Normal() {
    HomeScreenPreviewContent()
}

// превью с большим шрифтом
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

// превью на узком устройстве
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

@DrawableRes
private fun resolveAvatarRes(id: Int): Int = when (id) {
    0 -> R.drawable.avatar_0
    1 -> R.drawable.avatar_1
    2 -> R.drawable.avatar_2
    3 -> R.drawable.avatar_3
    4 -> R.drawable.avatar_4
    5 -> R.drawable.avatar_5
    6 -> R.drawable.avatar_6
    7 -> R.drawable.avatar_7
    8 -> R.drawable.avatar_8
    else -> R.drawable.img
}
