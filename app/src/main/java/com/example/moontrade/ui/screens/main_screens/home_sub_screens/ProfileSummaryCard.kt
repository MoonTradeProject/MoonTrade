package com.example.moontrade.ui.screens.main_screens.home_sub_screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import com.example.moontrade.ui.screens.components.glasskit.ActiveStatusPill
import com.example.moontrade.ui.screens.components.glasskit.AvatarWithRing
import com.example.moontrade.ui.screens.components.glasskit.GlassCard
import com.example.moontrade.ui.theme.TextPrimaryLight
import com.example.moontrade.ui.theme.extended
import com.example.moontrade.utils.parseBigDecimalLoose
import com.example.moontrade.utils.formatUsdFull
import com.example.moontrade.utils.formatUsdCompact
import com.example.moontrade.utils.formatPercent
import java.math.BigDecimal
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import com.example.moontrade.ui.theme.Violet200
import com.example.moontrade.ui.theme.Violet300

@Composable
private fun isDarkThemeFromColors(): Boolean {
    val cs = MaterialTheme.colorScheme
    return cs.background.luminance() < 0.5f
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileSummaryCard(
    nickname: String,
    selectedTags: List<String>,
    avatarId: Int,
    avatarUrl: String?,
    balanceText: String,
    roiValue: Double,
    roiLabel: String,
    avatarResIdFrom: (Int) -> Int
) {
    val cs = MaterialTheme.colorScheme
    val ex = MaterialTheme.extended
    val dark = isDarkThemeFromColors()

    val balanceAmount = remember(balanceText) { parseBigDecimalLoose(balanceText) }
    val isCompact = balanceAmount?.abs()?.let { it >= BigDecimal("100000") } == true
    val amountUi = balanceAmount?.let { amt ->
        if (isCompact) formatUsdCompact(amt) else formatUsdFull(amt)
    } ?: balanceText

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        corner = 22.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // ----- TOP PART -----
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                AvatarWithRing(
                    size = 100.dp,
                    innerColor = Color.Transparent,
                    borderPadding = 0.dp
                ) {
                    if (avatarId == -1 && !avatarUrl.isNullOrEmpty()) {
                        Image(
                            painter = rememberAsyncImagePainter(avatarUrl),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = avatarResIdFrom(avatarId)),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }
                }

                Spacer(Modifier.width(30.dp))

                Column(
                    Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val displayName = if (nickname.isBlank()) "TraderX" else nickname

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

                    Spacer(Modifier.height(8.dp))

                    ActiveStatusPill()

                    Spacer(Modifier.height(8.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        selectedTags.forEach {
                            AssistChip(
                                onClick = {},
                                label = {
                                    Text(
                                        it,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = ex.glassSurface,
                                    labelColor = cs.onSurface
                                )
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Divider
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

            // ----- BOTTOM: TOTAL VALUE + ROI -----
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(
                    Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        "TOTAL VALUE",
                        color = cs.onSurface.copy(alpha = .65f),
                        style = MaterialTheme.typography.labelLarge
                    )
                    val amountStyle = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontFeatureSettings = "tnum"
                    )
                    val currencyStyle = MaterialTheme.typography.labelLarge

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = amountUi,
                            style = amountStyle,
                            color = ex.positiveText,
                            maxLines = 1,
                            modifier = Modifier.alignByBaseline()
                        )

                        Spacer(Modifier.width(10.dp))

                        Text(
                            text = "USDT",
                            style = currencyStyle,
                            color = cs.onSurface.copy(alpha = 0.75f),
                            modifier = Modifier.alignByBaseline()
                        )
                    }
                }

                Spacer(Modifier.width(8.dp))

                RoiCard(
                    roi = formatPercent(roiValue, keepPlus = false, decimals = 1),
                    isPositive = roiValue > 0
                )
            }
        }
    }
}

@Composable
private fun RoiCard(roi: String, isPositive: Boolean) {
    val ex = MaterialTheme.extended
    val cs = MaterialTheme.colorScheme
    val dark = isDarkThemeFromColors()
    val shape = RoundedCornerShape(18.dp)

    val digitsStyle = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.SemiBold,
        fontFeatureSettings = "tnum"
    )

    val bgColor = if (dark) {
        cs.surface.copy(alpha = 0.25f)
    } else {
        Color.White
    }

    val borderColor = if (dark) {
        Violet300.copy(alpha = 1.2f)
    } else {
        Violet200.copy(alpha = 0.6f)
    }

    Surface(
        modifier = Modifier.widthIn(min = 120.dp),
        color = bgColor,
        shape = shape,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Column(
            Modifier
                .border(3.dp, borderColor, shape)
                .padding(vertical = 12.dp, horizontal = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "ROI",
                color = cs.onSurface.copy(1.7f),
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(Modifier.height(4.dp))
            Text(
                roi,
                style = digitsStyle,
                color = if (isPositive) ex.success else ex.danger,
                maxLines = 1
            )
        }
    }
}
