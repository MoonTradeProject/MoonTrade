package com.example.moontrade.ui.screens.main_screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.moontrade.model.Mode
import com.example.moontrade.model.WebSocketStatus
import com.example.moontrade.navigation.NavRoutes
import com.example.moontrade.ui.screens.components.PlayerCard
import com.example.moontrade.ui.theme.extended
import com.example.moontrade.viewmodels.*


data class SelectableMode(val mode: Mode, val label: String)

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
    // collect viewmodel data
    val leaderboardEntries by leaderboardViewModel.entries.collectAsState()
    val topPlayers = leaderboardEntries.take(5)
    val nickname by profileViewModel.nickname.collectAsState()
    val selectedTags by profileViewModel.selectedTags.collectAsState()
    val avatarId by profileViewModel.avatarId.collectAsState()
    val avatarUrl by profileViewModel.avatarUrl.collectAsState()
    val roi by balanceViewModel.roi.collectAsState()
    val currentMode by balanceViewModel.mode.collectAsState()
    val balance by balanceViewModel.balance.collectAsState()
    val status by balanceViewModel.status.collectAsState()
    val tournaments by tournamentsViewModel.tournaments.collectAsState()
    val userAssets by userAssetsViewModel.assets.collectAsState()

    val colors = MaterialTheme.extended

    // prepare selectable modes
    val selectableModes = listOf(SelectableMode(Mode.Main, "Main")) +
            tournaments.filter { it.isJoined }.map {
                SelectableMode(Mode.Tournament(it.id.toString()), it.name)
            }

    var selected by remember(currentMode, selectableModes) {
        mutableStateOf(selectableModes.find { it.mode == currentMode } ?: selectableModes.first())
    }

    // update logic
    val selectedMode = selected.mode
    LaunchedEffect(Unit) { balanceViewModel.connect() }
    LaunchedEffect(selectedMode) {
        balanceViewModel.changeMode(selectedMode)
        leaderboardViewModel.loadLeaderboard()
        userAssetsViewModel.loadUserAssets()
    }

    // UI
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.topBarBg,
                    titleContentColor = colors.topBarText,
                    actionIconContentColor = colors.topBarText
                ),
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        var expanded by remember { mutableStateOf(false) }

                        Box {
                            TextButton(onClick = { expanded = true }) {
                                Text(
                                    selected.label,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = colors.topBarText
                                )
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.background(colors.dropdownBg)
                            ) {
                                selectableModes.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(item.label, color = colors.topBarText) },
                                        onClick = {
                                            selected = item
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.weight(1f))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(
                                        color = when (status) {
                                            is WebSocketStatus.Connected -> Color(0xFF00E676)
                                            is WebSocketStatus.Connecting -> Color.Yellow
                                            is WebSocketStatus.Error -> Color.Red
                                            WebSocketStatus.Idle -> Color.Gray
                                        },
                                        shape = CircleShape
                                    )
                            )

                            Text(
                                balance,
                                style = MaterialTheme.typography.titleMedium,
                                color = colors.topBarText
                            )

                            IconButton(onClick = {
                                navController.navigate(NavRoutes.SETTINGS)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Settings",
                                    tint = colors.topBarText
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Avatar card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.avatarCard)
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (avatarId == -1 && !avatarUrl.isNullOrEmpty()) {
                            Log.d("AvatarDebug", "🖼 Loading custom avatar: $avatarUrl")

                            Image(
                                painter = rememberAsyncImagePainter(avatarUrl),
                                contentDescription = "Custom Avatar",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                            )
                        } else {
                            Image(
                                painter = painterResource(id = avatarResIdFrom(avatarId)),
                                contentDescription = "Built-in Avatar",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                nickname,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.nickname
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                selectedTags.forEach {
                                    AssistChip(
                                        onClick = {},
                                        label = { Text(it) },
                                        colors = AssistChipDefaults.assistChipColors(
                                            containerColor = colors.chip,
                                            labelColor = colors.chipText
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Portfolio card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.portfolioCard)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            "Portfolio",
                            fontSize = 16.sp,
                            color = colors.portfolioLabel
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            balance,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.gold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "ROI: $roi",
                            fontSize = 14.sp,
                            color = colors.portfolioRoi
                        )
                    }
                }
            }

            // Top players
            if (topPlayers.isNotEmpty()) {
                item {
                    Text("Top Traders", style = MaterialTheme.typography.titleLarge)
                }

                itemsIndexed(topPlayers) { index, entry ->
                    val medal = when (index) {
                        0 -> "\uD83E\uDD47" // 🥇
                        1 -> "\uD83E\uDD48" // 🥈
                        2 -> "\uD83E\uDD49" // 🥉
                        else -> null
                    }

                    PlayerCard(entry = entry, medal = medal) {
                        selectedPlayerViewModel.set(entry)
                        navController.navigate(NavRoutes.PLAYER_PROFILE)
                    }
                }
            }

            // Assets
            if (userAssets.isNotEmpty()) {
                item {
                    Text("Your Assets", style = MaterialTheme.typography.titleLarge)
                }

                items(userAssets) { asset ->
                    AssetCard(label = asset.asset_name, value = asset.amount.toPlainString())
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun AssetCard(label: String, value: String) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(label, style = MaterialTheme.typography.bodyLarge)
                Text(value, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}


