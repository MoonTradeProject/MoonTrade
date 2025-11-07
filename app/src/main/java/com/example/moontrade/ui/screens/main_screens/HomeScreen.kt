package com.example.moontrade.ui.screens.main_screens

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moontrade.R
import com.example.moontrade.model.Mode
import com.example.moontrade.navigation.NavRoutes
import com.example.moontrade.ui.screens.main_screens.home_sub_screens.*
import com.example.moontrade.viewmodels.*

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    balanceViewModel: BalanceViewModel,
    tournamentsViewModel: TournamentsViewModel,
    profileViewModel: ProfileViewModel,
    leaderboardViewModel: LeaderboardViewModel,
    userAssetsViewModel: UserAssetsViewModel,
    selectedPlayerViewModel: SelectedPlayerViewModel,
) {
    // Collecting reactive state from all ViewModels
    val leaderboardEntries by leaderboardViewModel.entries.collectAsState()
    val topPlayers = leaderboardEntries.take(5)

    val nickname by profileViewModel.nickname.collectAsState()
    val selectedTags by profileViewModel.selectedTags.collectAsState()
    val avatarId by profileViewModel.avatarId.collectAsState()
    val avatarUrl by profileViewModel.avatarUrl.collectAsState()

    val roi by balanceViewModel.roi.collectAsState()
    val currentMode by balanceViewModel.mode.collectAsState()
    val balance by balanceViewModel.balance.collectAsState()

    val tournaments by tournamentsViewModel.tournaments.collectAsState()
    val userAssets by userAssetsViewModel.assets.collectAsState()

    // Build list of available modes (Main + joined tournaments)
    val selectableModes = remember(tournaments) {
        listOf(SelectableMode(Mode.Main, "Main")) +
                tournaments.filter { it.isJoined }
                    .map { SelectableMode(Mode.Tournament(it.id.toString()), it.name) }
    }

    // Keep track of which mode is selected
    var selected by remember(currentMode, selectableModes) {
        mutableStateOf(selectableModes.find { it.mode == currentMode } ?: selectableModes.first())
    }

    // Connect to balance socket when HomeScreen appears
    LaunchedEffect(Unit) { balanceViewModel.connect() }

    // When mode changes, reload data for leaderboard and assets ---
    LaunchedEffect(selected.mode) {
        balanceViewModel.changeMode(selected.mode)
        leaderboardViewModel.loadLeaderboard()
        userAssetsViewModel.loadUserAssets()
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    //  Main screen layout
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            HomeTopBar(
                selected = selected,
                items = selectableModes,
                onSelect = { selected = it },
                onOpenSettings = { navController.navigate(NavRoutes.SETTINGS) }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Profile + Balance + ROI card
            item {
                val roiValue = roi.replace("%", "").replace(",", ".").toDoubleOrNull() ?: 0.0
                val roiLabel = (if (roiValue >= 0) "+" else "") + String.format("%.1f%%", roiValue)

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

            // Top 5 traders section
            TopTradersSection(
                top = topPlayers,
                onClickPlayer = {
                    selectedPlayerViewModel.set(it)
                    navController.navigate(NavRoutes.PLAYER_PROFILE)
                }
            )

            // User assets list
            AssetsSection(
                assets = userAssets.map { a ->
                    UserAssetUi(a.asset_name, a.amount)
                }
            )

            // Extra space at the bottom (for scroll padding)
            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

/* ---------------- Helper function ---------------- */

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
