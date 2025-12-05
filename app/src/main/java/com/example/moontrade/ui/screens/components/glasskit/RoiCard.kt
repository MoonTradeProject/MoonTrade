package com.example.moontrade.ui.screens.components.glasskit

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.moontrade.ui.theme.Violet200
import com.example.moontrade.ui.theme.Violet300
import com.example.moontrade.ui.theme.extended

@Composable
private fun isDarkThemeForRoi(): Boolean {
    val cs = MaterialTheme.colorScheme
    return cs.background.luminance() < 0.5f
}

@Composable
fun RoiCard(
    roi: String,
    isPositive: Boolean,
    modifier: Modifier = Modifier
) {
    val ex = MaterialTheme.extended
    val cs = MaterialTheme.colorScheme
    val dark = isDarkThemeForRoi()
    val shape = RoundedCornerShape(18.dp)

    val digitsStyle = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.SemiBold,
        fontFeatureSettings = "tnum"
    )
    val bgColor = if (dark) cs.surface.copy(alpha = 1.0f) else Color.White
    val borderColor = if (dark) Violet300.copy(alpha = 1.2f) else Violet200.copy(alpha = 0.6f)

    Surface(
        modifier = modifier.widthIn(min = 120.dp, max = 160.dp),
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
                text = "ROI",
                color = cs.onSurface,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = roi,
                style = digitsStyle,
                color = if (isPositive) ex.success else ex.danger,
                maxLines = 1,
                softWrap = false
            )
        }
    }
}
