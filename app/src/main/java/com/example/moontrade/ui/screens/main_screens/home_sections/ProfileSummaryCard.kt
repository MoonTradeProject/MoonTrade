package com.example.moontrade.ui.screens.main_screens.home_sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.moontrade.ui.screens.components.glasskit.AvatarWithRing
import com.example.moontrade.ui.screens.components.glasskit.GlassCard
import com.example.moontrade.ui.screens.components.glasskit.RoiCard
import com.example.moontrade.ui.screens.components.glasskit.TagChip
import com.example.moontrade.ui.screens.components.glasskit.TotalValueCard
import com.example.moontrade.ui.theme.TextPrimaryLight
import com.example.moontrade.utils.formatPercent
import com.example.moontrade.utils.formatUsdCompact
import com.example.moontrade.utils.formatUsdFull
import com.example.moontrade.utils.parseBigDecimalLoose
import java.math.BigDecimal

@Composable
fun ProfileSummaryCard(
    nickname: String,
    selectedTags: List<String>,
    avatarId: Int,
    avatarUrl: String?,
    balanceText: String,
    roiValue: Double,
    avatarResIdFrom: (Int) -> Int
) {
    val cs = MaterialTheme.colorScheme
    val dark = cs.background.luminance() < 0.5f

    val balanceAmount = remember(balanceText) { parseBigDecimalLoose(balanceText) }
    val isCompact = balanceAmount?.abs()?.let { it >= BigDecimal("100000") } == true
    val amountUi = balanceAmount?.let { amt ->
        if (isCompact) formatUsdCompact(amt) else formatUsdFull(amt)
    } ?: balanceText

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        corner = 22.dp
    ) {
        Column(Modifier.fillMaxWidth()) {

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(end = 4.dp),
                verticalAlignment = Alignment.Top
            ) {

                AvatarWithRing(
                    size = 88.dp,
                    innerColor = Color.Transparent,
                    borderPadding = 0.dp
                ) {
                    val painter = if (!avatarUrl.isNullOrEmpty()) {
                        rememberAsyncImagePainter(avatarUrl)
                    } else {
                        painterResource(id = avatarResIdFrom(avatarId))
                    }

                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }
                Spacer(Modifier.width(16.dp))

                Column(
                    Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val displayName = nickname.ifBlank { "TraderX" }

                    Text(
                        text = displayName,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            shadow = Shadow(
                                color = if (dark)
                                    Color.Black.copy(alpha = 0.35f)
                                else
                                    Color.Black.copy(alpha = 0.10f),
                                offset = Offset(0f, 2f),
                                blurRadius = 1f
                            )
                        ),
                        color = if (dark) cs.onSurface.copy(alpha = 0.9f) else TextPrimaryLight,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (selectedTags.isNotEmpty()) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            selectedTags.forEach { tag ->
                                TagChip(text = tag)
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                cs.onSurface.copy(0.03f),
                                cs.onSurface.copy(0.10f),
                                cs.onSurface.copy(0.03f)
                            )
                        )
                    )
            )
            Spacer(Modifier.height(16.dp))

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                TotalValueCard(
                    amountUi = amountUi,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )
                RoiCard(
                    roi = formatPercent(roiValue, keepPlus = false, decimals = 1),
                    isPositive = roiValue > 0
                )
            }
        }
    }
}
