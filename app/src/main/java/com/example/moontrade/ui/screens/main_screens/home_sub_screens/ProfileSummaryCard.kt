package com.example.moontrade.ui.screens.main_screens.home_sub_screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.moontrade.ui.screens.components.glasskit.*
import com.example.moontrade.ui.theme.extended
import com.example.moontrade.utils.parseBigDecimalLoose
import com.example.moontrade.utils.formatUsdFull
import com.example.moontrade.utils.formatUsdCompact
import com.example.moontrade.utils.formatPercent
import java.math.BigDecimal

@Composable
fun ProfileSummaryCard(
    nickname: String,
    selectedTags: List<String>,
    avatarId: Int,
    avatarUrl: String?,
    balanceText: String,
    roiValue: Double,
    roiLabel: String,          // don't use
    avatarResIdFrom: (Int) -> Int
) {
    val cs = MaterialTheme.colorScheme
    val ex = MaterialTheme.extended

    val balanceAmount = remember(balanceText) { parseBigDecimalLoose(balanceText) }
    val isCompact = balanceAmount?.abs()?.let { it >= BigDecimal("100000") } == true
    val amountUi = balanceAmount?.let { amt ->
        if (isCompact) formatUsdCompact(amt) else formatUsdFull(amt)
    } ?: balanceText

    GlassCard(overlay = ex.gradientAccent) {
        // top part
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            AvatarWithRing {
                if (avatarId == -1 && !avatarUrl.isNullOrEmpty()) {
                    Image(
                        rememberAsyncImagePainter(avatarUrl),
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

            Spacer(Modifier.width(16.dp))

            Column(Modifier.weight(1f)) {
                GradientText(
                    text = if (nickname.isBlank()) "CryptoMaster" else nickname,
                    brush = ex.gradientAvatar,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.height(8.dp))
                Pill("ACTIVE", ex.gradientPrimary, leadingDot = true)
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
        Box(
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    Brush.linearGradient(
                        listOf(
                            Color.White.copy(.04f),
                            Color.White.copy(.12f),
                            Color.White.copy(.04f)
                        )
                    )
                )
        )
        Spacer(Modifier.height(16.dp))

        // bottom Total + ROI
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

                // amount + USDT
                val amountStyle = MaterialTheme.typography.headlineLarge.copy(
                    fontFeatureSettings = "tnum"
                )
                val currencyStyle = MaterialTheme.typography.labelLarge

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        text = amountUi,
                        style = amountStyle,
                        color = Color.White,
                        maxLines = 1,
                        modifier = Modifier.alignByBaseline()
                    )

                    Spacer(Modifier.width(4.dp))

                    Text(
                        text = "USDT",
                        style = currencyStyle,
                        color = cs.onSurface.copy(alpha = 0.75f),
                        modifier = Modifier.alignByBaseline()
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            RoiCard(
                roi = formatPercent(roiValue, keepPlus = false, decimals = 1),
                isPositive = roiValue > 0
            )
        }
    }
}

@Composable
private fun RoiCard(roi: String, isPositive: Boolean) {
    val ex = MaterialTheme.extended
    val cs = MaterialTheme.colorScheme
    val shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp)
    val digitsStyle = MaterialTheme.typography.titleLarge.copy(fontFeatureSettings = "tnum")

    Surface(
        modifier = Modifier
            .widthIn(min = 120.dp)
            .clip(shape)
            .background(ex.glassSurface, shape)
            .border(
                1.dp,
                Brush.linearGradient(listOf(Color(0x66B08BFF), Color(0x334E2EA8))),
                shape
            ),
        color = Color.Transparent,
        shape = shape
    ) {
        Column(
            Modifier.padding(vertical = 12.dp, horizontal = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("ROI", color = cs.onSurface.copy(.7f), style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.height(4.dp))
            Text(
                roi, //  -0.9%
                style = digitsStyle,
                color = if (isPositive) ex.success else ex.danger,
                maxLines = 1
            )
        }
    }
}
