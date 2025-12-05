package com.example.moontrade.ui.screens.main_screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moontrade.R
import com.example.moontrade.model.Mode
import com.example.moontrade.model.LeaderboardEntry
import com.example.moontrade.navigation.NavRoutes
import com.example.moontrade.ui.screens.components.bars.TopBar
import com.example.moontrade.ui.screens.components.bars.SectionHeader
import com.example.moontrade.ui.screens.main_screens.home_sections.*
import com.example.moontrade.ui.screens.main_screens.order_sub_screen.MyOrdersCard
import com.example.moontrade.ui.screens.components.glasskit.resolveAvatarRes
import com.example.moontrade.viewmodels.*

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
fun HomeScreenContent(
    nickname: String,
    selectedTags: List<String>,
    avatarId: Int,
    avatarUrl: String?,
    balanceText: String,
    roiValue: Double,
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
                ModeSelector(
                    selected = selectedMode,
                    items = selectableModes,
                    onSelect = onSelectMode,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))

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

