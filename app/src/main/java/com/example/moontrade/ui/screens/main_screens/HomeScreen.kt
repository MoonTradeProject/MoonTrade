package com.example.moontrade.ui.screens.main_screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.moontrade.model.Mode
import com.example.moontrade.model.WebSocketStatus
import com.example.moontrade.navigation.NavRoutes
import com.example.moontrade.ui.screens.components.PlayerCard
import com.example.moontrade.ui.screens.components.bars.TopBar
import com.example.moontrade.ui.theme.extended
import com.example.moontrade.viewmodels.*
import com.google.accompanist.flowlayout.FlowRow

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
    // --- state from viewmodels ---
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

    val cs = MaterialTheme.colorScheme
    val ex = MaterialTheme.extended

    // список режимов
    val selectableModes = remember(tournaments) {
        listOf(SelectableMode(Mode.Main, "Main")) +
                tournaments.filter { it.isJoined }
                    .map { SelectableMode(Mode.Tournament(it.id.toString()), it.name) }
    }

    var selected by remember(currentMode, selectableModes) {
        mutableStateOf(selectableModes.find { it.mode == currentMode } ?: selectableModes.first())
    }

    // side effects
    val selectedMode = selected.mode
    LaunchedEffect(Unit) { balanceViewModel.connect() }
    LaunchedEffect(selectedMode) {
        balanceViewModel.changeMode(selectedMode)
        leaderboardViewModel.loadLeaderboard()
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
                navigationContent = { // ⬅️ слева селектор режимов
                    var expanded by remember { mutableStateOf(false) }

                    Box {
                        TextButton(onClick = { expanded = true }) {
                            Text(
                                text = selected.label,
                                style = MaterialTheme.typography.titleMedium,
                                color = cs.onSurface
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(ex.glassSurface)
                        ) {
                            selectableModes.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item.label, color = cs.onSurface) },
                                    onClick = {
                                        selected = item
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                },
                centerContent = { // ⬅️ по центру — MOONTRADE
                    val label = buildAnnotatedString {
                        withStyle(SpanStyle(brush = ex.gradientAccent)) {
                            append("MOONTRADE")
                        }
                    }
                    Text(
                        text = label,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 20.sp,
                        maxLines = 1
                    )
                },
                actions = { // ⬅️ справа — настройки
                    IconButton(onClick = { navController.navigate(NavRoutes.SETTINGS) }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(cs.background)
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 2) Профиль
            item {
                GlassCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (avatarId == -1 && !avatarUrl.isNullOrEmpty()) {
                            Image(
                                painter = rememberAsyncImagePainter(avatarUrl),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                            )
                        } else {
                            Image(
                                painter = painterResource(id = avatarResIdFrom(avatarId)),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                            )
                        }

                        Spacer(Modifier.width(16.dp))

                        Column(Modifier.weight(1f)) {
                            Text(nickname, style = MaterialTheme.typography.titleLarge, color = cs.onSurface)
                            Spacer(Modifier.height(8.dp))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                selectedTags.forEach {
                                    AssistChip(
                                        onClick = {},
                                        label = { Text(it) },
                                        colors = AssistChipDefaults.assistChipColors(
                                            containerColor = ex.glassSurface,
                                            labelColor = cs.onSurface
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 3) Портфель
            item {
                GlassCard(overlay = ex.gradientAccent) {
                    Text("TOTAL VALUE", color = cs.onSurface.copy(alpha = .65f), fontSize = 16.sp)
                    Spacer(Modifier.height(6.dp))
                    Text(balance, style = MaterialTheme.typography.displaySmall, color = cs.onSurface)
                    Spacer(Modifier.height(6.dp))
                    val roiValue = roi?.replace("%", "")?.replace(",", ".")?.toDoubleOrNull() ?: 0.0
                    Text(
                        "ROI: $roi",
                        color = if (roiValue < 0) ex.danger else ex.success
                    )
                }
            }

            // 4) Топ-трейдеры
            if (topPlayers.isNotEmpty()) {
                item { Text("Top Traders", style = MaterialTheme.typography.titleLarge, color = cs.onBackground) }
                itemsIndexed(topPlayers) { index, entry ->
                    val medal = when (index) { 0 -> "🥇"; 1 -> "🥈"; 2 -> "🥉"; else -> null }
                    PlayerCard(entry = entry, medal = medal) {
                        selectedPlayerViewModel.set(entry)
                        navController.navigate(NavRoutes.PLAYER_PROFILE)
                    }
                }
            }

            // 5) Активы
            if (userAssets.isNotEmpty()) {
                item { Text("Your Assets", style = MaterialTheme.typography.titleLarge, color = cs.onBackground) }
                items(userAssets) { asset ->
                    AssetRow(label = asset.asset_name, value = asset.amount.toPlainString())
                }
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

/* ---------------- helpers ---------------- */

@Composable
private fun GlassCard(
    modifier: Modifier = Modifier,
    corner: Dp = 22.dp,
    overlay: Brush? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val ex = MaterialTheme.extended
    val shape = RoundedCornerShape(corner)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(16.dp, shape, clip = false)
            .clip(shape)
            .background(ex.glassCard)
            .then(if (overlay != null) Modifier.background(overlay, shape) else Modifier)
            .border(
                1.dp,
                Brush.linearGradient(
                    listOf(
                        Color.White.copy(alpha = 0.18f),
                        Color.White.copy(alpha = 0.06f)
                    )
                ),
                shape
            ),
        color = Color.Transparent,
        contentColor = cs.onSurface,
        shape = shape
    ) {
        Column(Modifier.padding(18.dp), content = content)
    }
}

@Composable
private fun StatusDot(status: WebSocketStatus) {
    val ex = MaterialTheme.extended
    val cs = MaterialTheme.colorScheme
    val color = when (status) {
        is WebSocketStatus.Connected -> ex.success
        is WebSocketStatus.Connecting -> ex.warning
        is WebSocketStatus.Error -> ex.danger
        WebSocketStatus.Idle -> cs.outline
    }
    Box(Modifier.size(10.dp).clip(CircleShape).background(color))
}

@Composable
private fun AssetRow(label: String, value: String) {
    val cs = MaterialTheme.colorScheme
    GlassCard {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(label, style = MaterialTheme.typography.bodyLarge, color = cs.onSurface)
                Text(value, style = MaterialTheme.typography.bodyMedium, color = cs.onSurface.copy(alpha = .75f))
            }
        }
    }
}
