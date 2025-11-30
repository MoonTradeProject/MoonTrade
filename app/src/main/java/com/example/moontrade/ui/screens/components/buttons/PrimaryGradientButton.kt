package com.example.moontrade.ui.screens.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.moontrade.ui.theme.Violet300
import com.example.moontrade.ui.theme.Violet600
@Composable
fun PrimaryGradientButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isSecondary: Boolean = false,
) {
    val cs = MaterialTheme.colorScheme
    val dark = cs.background.luminance() < 0.5f

    val baseColors = if (dark) {
        listOf(Violet600, Violet300.copy(alpha = 0.9f), Violet600)
    } else {
        listOf(Violet600, Violet600.copy(alpha = 0.7f), Violet600)
    }

    val gradient = Brush.horizontalGradient(
        if (isSecondary) baseColors.map { it.copy(alpha = 0.5f) } else baseColors
    )

    val textColor = if (isSecondary) {
        cs.onPrimary.copy(alpha = 0.85f)
    } else {
        cs.onPrimary
    }

    Box(
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(gradient)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = textColor
        )
    }
}
