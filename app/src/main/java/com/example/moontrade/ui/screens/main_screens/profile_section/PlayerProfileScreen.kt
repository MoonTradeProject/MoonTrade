@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.example.moontrade.ui.screens.main_screens.profile_section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moontrade.ui.screens.components.bars.SectionHeader
import com.example.moontrade.ui.screens.components.bars.TopBar
import com.example.moontrade.ui.screens.components.glasskit.RoiCard
import com.example.moontrade.ui.screens.components.glasskit.TagChip
import com.example.moontrade.utils.formatPercent
import com.example.moontrade.viewmodels.SelectedPlayerViewModel

@Composable
fun PlayerProfileScreen(
    navController: NavController,
    selectedPlayerViewModel: SelectedPlayerViewModel
) {
    val player by selectedPlayerViewModel.selected.collectAsState()
    val cs = MaterialTheme.colorScheme

    if (player == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No player selected.")
        }
        return
    }

    val roiValue = player!!.roi
    val roiDisplay = formatPercent(
        value = roiValue,
        keepPlus = true,
        decimals = 1
    )

    Scaffold(
        topBar = {
            TopBar(
                title = player!!.username,
                showBack = true,
                onBack = { navController.popBackStack() }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            cs.background,
                            cs.background.copy(alpha = 0.92f)
                        )
                    )
                )
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // ───── Avatar + Rank + Tags ─────
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ProfileAvatar(
                        avatarUrl = player!!.avatar_url,
                        avatarId = -1
                    )
                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = "Rank ${player!!.rank}",
                        style = MaterialTheme.typography.titleMedium,
                        color = cs.onSurface.copy(alpha = 0.85f)
                    )

                    Spacer(Modifier.height(12.dp))

                    if (player!!.tags.isNotEmpty()) {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            player!!.tags.forEach { TagChip(text = it) }
                        }
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RoiCard(
                        roi = roiDisplay,
                        isPositive = roiValue > 0
                    )
                }
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    SectionHeader("About me")
                    Text(
                        text = player!!.description.orEmpty(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = cs.onSurface.copy(alpha = 0.9f)
                    )
                }
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SectionHeader("Achievements")

                    if (player!!.achievements.isEmpty()) {
                        Text(
                            "No achievements yet.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = cs.onSurface.copy(alpha = 0.6f)
                        )
                        AchievementBadge("Coming soon…")
                    } else {
                        player!!.achievements.forEach {
                            AchievementBadge(it)
                        }
                    }
                }
            }
        }
    }
}
