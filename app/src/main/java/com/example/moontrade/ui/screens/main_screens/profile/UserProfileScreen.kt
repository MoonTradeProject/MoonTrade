@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
package com.example.moontrade.ui.screens.main_screens.profile

import android.view.MotionEvent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.moontrade.R
import com.example.moontrade.navigation.NavRoutes
import com.example.moontrade.ui.screens.components.bars.TopBar
import com.example.moontrade.ui.screens.components.glasskit.GlassCard
import com.example.moontrade.ui.screens.components.glasskit.RoiCard
import com.example.moontrade.ui.screens.components.glasskit.TagChip
import com.example.moontrade.ui.screens.components.glasskit.TotalValueCard
import com.example.moontrade.ui.screens.components.glasskit.resolveAvatarRes
import com.example.moontrade.utils.formatPercent
import com.example.moontrade.utils.formatUsdCompact
import com.example.moontrade.utils.formatUsdFull
import com.example.moontrade.utils.parseBigDecimalLoose
import com.example.moontrade.viewmodels.BalanceViewModel
import com.example.moontrade.viewmodels.ProfileViewModel
import java.math.BigDecimal

@Composable
fun UserProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel,
    balanceViewModel: BalanceViewModel
) {
    val nickname     by profileViewModel.nickname.collectAsState()
    val description  by profileViewModel.description.collectAsState()
    val selectedTags by profileViewModel.selectedTags.collectAsState()
    val avatarId     by profileViewModel.avatarId.collectAsState()
    val avatarUrl    by profileViewModel.avatarUrl.collectAsState()

    val balanceText  by balanceViewModel.balance.collectAsState()
    val roiRaw       by balanceViewModel.roi.collectAsState()

    val roiValue = roiRaw
        .replace("%", "")
        .replace(",", ".")
        .toDoubleOrNull() ?: 0.0

    val roiDisplay = formatPercent(
        value = roiValue,
        keepPlus = false,
        decimals = 1
    )
    val balanceAmount = remember(balanceText) { parseBigDecimalLoose(balanceText) }
    val isCompact = balanceAmount?.abs()?.let { it >= BigDecimal("100000") } == true

    val formattedBalance = balanceAmount?.let {
        if (isCompact) formatUsdCompact(it) else formatUsdFull(it)
    } ?: balanceText

    UserProfileScreenContent(
        nickname = nickname,
        description = description,
        selectedTags = selectedTags,
        avatarId = avatarId,
        avatarUrl = avatarUrl,
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
                            contentDescription = "Edit",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(40.dp)
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
                        listOf(
                            cs.background,
                            cs.background.copy(alpha = 0.92f)
                        )
                    )
                )
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    ProfileAvatar(avatarUrl, avatarId)
                    Text(
                        text = nickname.ifBlank { "TraderX" },
                        style = MaterialTheme.typography.headlineSmall.copy(
                            shadow = Shadow(
                                color = Color.Black.copy(alpha = 0.35f),
                                offset = Offset(0f, 2f),
                                blurRadius = 1f
                            )
                        ),
                        color = cs.onSurface.copy(alpha = 0.9f)
                    )
                    if (selectedTags.isNotEmpty()) {
                        Spacer(Modifier.height(6.dp))

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            selectedTags.forEach { tag ->
                                TagChip(text = tag)
                            }
                        }
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TotalValueCard(
                        amountUi = formattedBalance,
                        modifier = Modifier
                            .weight(1f)

                    )
                    RoiCard(
                        roi = roiDisplay,
                        isPositive = roiValue > 0,
                        modifier = Modifier.padding(end = 10.dp)

                    )
                }
            }
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    corner = 20.dp
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "Bio",
                            style = MaterialTheme.typography.titleMedium,
                            color = cs.onSurface
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = description.ifBlank { "No bio yet." },
                            style = MaterialTheme.typography.bodyMedium,
                            color = cs.onSurface.copy(alpha = if (description.isBlank()) 0.55f else 0.9f),
                            lineHeight = 20.sp
                        )
                    }
                }
            }
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    corner = 20.dp
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "Achievements",
                            style = MaterialTheme.typography.titleMedium,
                            color = cs.onSurface
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "Profile milestones will appear here.",
                            style = MaterialTheme.typography.bodySmall,
                            color = cs.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(Modifier.height(12.dp))
                        AchievementBadge("Coming soon…")
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileAvatar(
    avatarUrl: String?,
    avatarId: Int,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme

    var pressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (pressed) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = 0.6f,
            stiffness = 350f
        ),
        label = "avatarPressScale"
    )
    Box(
        modifier = modifier
            .size(200.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .pointerInteropFilter { event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        pressed = true
                        true
                    }
                    MotionEvent.ACTION_UP,
                    MotionEvent.ACTION_CANCEL -> {
                        pressed = false
                        true
                    }
                    else -> false
                }
            }
            .background(
                Brush.radialGradient(
                    listOf(
                        cs.primary.copy(alpha = 0.45f),
                        Color.Transparent
                    )
                ),
                CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, cs.primary, CircleShape)
                .background(cs.surfaceVariant.copy(alpha = 0.6f))
        ) {
            if (avatarId == -1 && !avatarUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = avatarUrl,
                    contentScale = ContentScale.Crop,
                    contentDescription = "avatar",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Image(
                    painter = painterResource(resolveAvatarRes(avatarId)),
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun AchievementBadge(title: String) {
    val cs = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(50)
    Row(
        modifier = Modifier
            .clip(shape)
            .background(cs.surface.copy(alpha = 0.4f))
            .border(
                width = 1.2.dp,
                brush = Brush.horizontalGradient(
                    listOf(
                        cs.primary,
                        cs.primary.copy(alpha = 0.3f),
                        cs.primary
                    )
                ),
                shape = shape
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.Star,
            contentDescription = null,
            tint = cs.primary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = cs.onSurface
        )
    }
}
