package com.example.moontrade.ui.screens.main_screens.home_sub_screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.moontrade.ui.screens.components.glasskit.*
import com.example.moontrade.ui.theme.extended
import com.google.accompanist.flowlayout.FlowRow

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

    GlassCard(overlay = ex.gradientAccent) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            AvatarWithRing {
                if (avatarId == -1 && !avatarUrl.isNullOrEmpty()) {
                    Image(
                        rememberAsyncImagePainter(avatarUrl),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize().clip(CircleShape)
                    )

                } else {
                    Image(
                        painter = painterResource(id = avatarResIdFrom(avatarId)),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize().clip(CircleShape)
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

        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("TOTAL VALUE", color = cs.onSurface.copy(alpha = .65f), style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(8.dp))
                Text(balanceText, style = MaterialTheme.typography.displaySmall, color = Color.White)
                Spacer(Modifier.height(6.dp))
                Text(
                    text = (if (roiValue >= 0) "+" else "-") + "$1 234,56 24H",
                    color = if (roiValue >= 0) ex.success else ex.danger,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(Modifier.width(12.dp))
            RoiCard(roi = roiLabel)
        }
    }
}

@Composable
private fun RoiCard(roi: String) {
    val ex = MaterialTheme.extended
    val cs = MaterialTheme.colorScheme
    val shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp)

    Surface(
        modifier = Modifier
            .widthIn(min = 128.dp)
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
            Modifier.padding(vertical = 14.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("ROI", color = cs.onSurface.copy(.7f), style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.height(6.dp))
            Text(roi, style = MaterialTheme.typography.titleLarge, color = ex.success)
        }
    }
}
