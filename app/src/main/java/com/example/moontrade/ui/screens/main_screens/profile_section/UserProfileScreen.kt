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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moontrade.R
import com.example.moontrade.navigation.NavRoutes
import com.example.moontrade.ui.screens.components.bars.SectionHeader
import com.example.moontrade.ui.screens.components.bars.TopBar
import com.example.moontrade.ui.screens.components.glasskit.TagChip
import com.example.moontrade.ui.screens.components.glasskit.TotalValueCard
import com.example.moontrade.ui.screens.components.glasskit.RoiCard
import com.example.moontrade.ui.screens.components.glasskit.GlassCard
import com.example.moontrade.utils.formatPercent
import com.example.moontrade.utils.formatUsdCompact
import com.example.moontrade.utils.formatUsdFull
import com.example.moontrade.utils.parseBigDecimalLoose
import com.example.moontrade.viewmodels.ProfileViewModel
import com.example.moontrade.viewmodels.BalanceViewModel
import java.math.BigDecimal

@Composable
fun UserProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel,
    balanceViewModel: BalanceViewModel
) {
    val nickname by profileViewModel.nickname.collectAsState()
    val description by profileViewModel.description.collectAsState()
    val selectedTags by profileViewModel.selectedTags.collectAsState()
    val avatarId by profileViewModel.avatarId.collectAsState()
    val avatarUrl by profileViewModel.avatarUrl.collectAsState()

    val balanceText by balanceViewModel.balance.collectAsState()
    val roiRaw by balanceViewModel.roi.collectAsState()

    val roiValue = roiRaw.replace("%", "").replace(",", ".").toDoubleOrNull() ?: 0.0
    val roiDisplay = formatPercent(roiValue, keepPlus = false, decimals = 1)

    val balanceAmount = parseBigDecimalLoose(balanceText)
    val formattedBalance =
        balanceAmount?.let {
            if (it >= BigDecimal("100000")) formatUsdCompact(it)
            else formatUsdFull(it)
        } ?: balanceText

    UserProfileScreenContent(
        nickname = nickname,
        description = description,
        selectedTags = selectedTags,
        avatarUrl = avatarUrl,
        avatarId = avatarId,
        formattedBalance = formattedBalance,
        roiDisplay = roiDisplay,
        roiValue = roiValue,
        onBack = { navController.popBackStack() },
        onEdit = { navController.navigate(NavRoutes.PROFILE_EDIT) }
    )
}

@Composable
private fun UserProfileScreenContent(
    nickname: String,
    description: String,
    selectedTags: List<String>,
    avatarId: Int,
    avatarUrl: String?,
    formattedBalance: String,
    roiDisplay: String,
    roiValue: Double,
    onBack: () -> Unit,
    onEdit: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            TopBar(
                title = "Profile",
                showBack = true,
                onBack = onBack,
                actions = {
                    IconButton(onClick = onEdit) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_edit),
                            contentDescription = "Edit"
                        )
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(cs.background, cs.background.copy(alpha = 0.92f))
                    )
                )
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // AVATAR + TAGS
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    ProfileAvatar(
                        avatarUrl = avatarUrl,
                        avatarId = avatarId
                    )
                    Spacer(Modifier.height(12.dp))

                    Text(
                        nickname.ifBlank { "TraderX" },
                        style = MaterialTheme.typography.titleLarge,
                        color = cs.onSurface.copy(alpha = 0.9f)
                    )

                    if (selectedTags.isNotEmpty()) {
                        Spacer(Modifier.height(6.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.Center,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            selectedTags.forEach { TagChip(it) }
                        }
                    }
                }
            }
            // BALANCE + ROI
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TotalValueCard(
                        amountUi = formattedBalance,
                        modifier = Modifier.weight(1f)
                    )
                    RoiCard(
                        roi = roiDisplay,
                        isPositive = roiValue > 0,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                }
            }
            // BIO
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SectionHeader("About me")

                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        corner = 24.dp
                    ) {
                        Text(
                            text = description.ifBlank { "Add a few lines about yourself" },
                            style = MaterialTheme.typography.bodyMedium,
                            color = cs.onSurface.copy(
                                alpha = if (description.isBlank()) 0.6f else 0.9f
                            ),
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(
                                horizontal = 14.dp,
                                vertical = 8.dp
                            )
                        )
                    }
                }
            }
            // ACHIEVEMENTS
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SectionHeader("Achievements")

                    Text(
                        "Profile milestones will appear here.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = cs.onSurface.copy(alpha = 0.6f)
                    )

                    AchievementBadge("Coming soon…")
                }
            }
        }
    }
}
