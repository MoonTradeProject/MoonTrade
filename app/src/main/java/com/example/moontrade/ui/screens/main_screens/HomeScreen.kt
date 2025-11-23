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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moontrade.R
import com.example.moontrade.model.Mode
import com.example.moontrade.navigation.NavRoutes
import com.example.moontrade.ui.screens.components.bars.TopBar
import com.example.moontrade.ui.screens.components.bars.SectionHeader
import com.example.moontrade.ui.screens.main_screens.home_sub_screens.*
import com.example.moontrade.viewmodels.*
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
        listOf(SelectableMode(Mode.Main, "Main")) +
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

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopBar(
                title = null,
                showBack = false,
                navigationContent = {
                    ModeSelector(
                        selected = selected,
                        items = selectableModes,
                        onSelect = { selected = it }
                    )
                },
                actions = {
                    IconButton(onClick = { navController.navigate(NavRoutes.SETTINGS) }) {
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
                val roiValue = roi
                    .replace("%", "")
                    .replace(",", ".")
                    .toDoubleOrNull() ?: 0.0

                val roiLabel = (if (roiValue >= 0) "+" else "") +
                        String.format(Locale.US, "%.1f%%", roiValue)

                ProfileSummaryCard(
                    nickname = nickname,
                    selectedTags = selectedTags,
                    avatarId = avatarId,
                    avatarUrl = avatarUrl,
                    balanceText = balance,
                    roiValue = roiValue,
                    roiLabel = roiLabel,
                    avatarResIdFrom = ::resolveAvatarRes
                )
            }

            item {
                SectionHeader("Your assets")
                AssetsSection(
                    assets = userAssets.map { a ->
                        UserAssetUi(
                            name = a.asset_name,
                            amount = a.amount,
                            assetValue = a.asset_value,
                            change = listOf(-2.5, 1.2, 0.8, -0.6).random()
                        )
                    },
                    isLoading = isLoading,
                    error = assetsError
                )
            }

            item {
                SectionHeader("Top players")
                TopPlayersCarousel(
                    entries = leaderboardEntries,
                    onClickPlayer = { entry ->
                        selectedPlayerViewModel.set(entry)
                        navController.navigate(NavRoutes.PLAYER_PROFILE)
                    }
                )
            }

            item { Spacer(Modifier.height(60.dp)) }

            item {
                Button(
                    onClick = { navController.navigate(NavRoutes.USER_ORDERS) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("My Orders")
                }
            }
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
