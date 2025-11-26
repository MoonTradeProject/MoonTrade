package com.example.moontrade.ui.screens.main_screens.home_sub_screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.foundation.layout.BoxWithConstraints
import coil.compose.rememberAsyncImagePainter
import com.example.moontrade.ui.screens.components.glasskit.ActiveStatusPill
import com.example.moontrade.ui.screens.components.glasskit.AvatarWithRing
import com.example.moontrade.ui.screens.components.glasskit.GlassCard
import com.example.moontrade.ui.theme.TextPrimaryLight
import com.example.moontrade.ui.theme.Violet200
import com.example.moontrade.ui.theme.Violet300
import com.example.moontrade.ui.theme.Violet600
import com.example.moontrade.ui.theme.extended
import com.example.moontrade.utils.formatPercent
import com.example.moontrade.utils.formatUsdCompact
import com.example.moontrade.utils.formatUsdFull
import com.example.moontrade.utils.parseBigDecimalLoose
import java.math.BigDecimal

@Composable
private fun isDarkThemeFromColors(): Boolean {
    val cs = MaterialTheme.colorScheme
    return cs.background.luminance() < 0.5f
}

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

                Spacer(Modifier.width(16.dp))

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

                    ActiveStatusPill()

                    if (selectedTags.isNotEmpty()) {
                        val bg = Brush.horizontalGradient(
                            listOf(
                                Violet600.copy(alpha = 0.6f),
                                Violet600.copy(alpha = 0.9f),
                                Violet600.copy(alpha = 1.2f)
                            )
                        )
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            selectedTags.forEach { tag ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(30))
                                        .background(bg)
                                        .padding(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        tag,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Color.White,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
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

            // ====================== BOTTOM ======================
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    Text(
                        "TOTAL VALUE",
                        color = cs.onSurface.copy(alpha = .65f),
                        style = MaterialTheme.typography.labelLarge
                    )

                    val amountStyle = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontFeatureSettings = "tnum"
                    )
                    val currencyStyle = MaterialTheme.typography.labelLarge

                    // АДАПТИВ!
                    BoxWithConstraints {
                        val isNarrow = maxWidth < 240.dp

                        if (isNarrow) {
                            // на маленьких устройствах — показываем сумму полностью
                            Column(
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = amountUi,
                                    style = amountStyle,
                                    color = ex.positiveText,
                                    maxLines = 1
                                )
                                Text(
                                    text = "USDT",
                                    style = currencyStyle,
                                    color = cs.onSurface.copy(alpha = 0.75f),
                                    maxLines = 1
                                )
                            }
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = amountUi,
                                    style = amountStyle,
                                    color = ex.positiveText,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .weight(1f)
                                        .alignByBaseline()
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "USDT",
                                    style = currencyStyle,
                                    color = cs.onSurface.copy(alpha = 0.75f),
                                    maxLines = 1,
                                    softWrap = false,
                                    overflow = TextOverflow.Clip,
                                    modifier = Modifier.alignByBaseline()
                                )
                            }
                        }
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

    val bgColor = if (dark) cs.surface.copy(alpha = 0.25f) else Color.White
    val borderColor = if (dark) Violet300.copy(alpha = 1.2f) else Violet200.copy(alpha = 0.6f)

    Surface(
        modifier = Modifier.widthIn(min = 120.dp, max = 160.dp),
        color = bgColor,
        shape = shape
    ) {
        Column(
            Modifier
                .border(3.dp, borderColor, shape)
                .padding(vertical = 12.dp, horizontal = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "ROI",
                color = cs.onSurface.copy(1.0f),
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1
            )
            Spacer(Modifier.height(4.dp))
            Text(
                roi,
                style = digitsStyle,
                color = if (isPositive) ex.success else ex.danger,
                maxLines = 1,
                softWrap = false
            )
        }
    }
}
