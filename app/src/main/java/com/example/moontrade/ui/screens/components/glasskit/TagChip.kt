package com.example.moontrade.ui.screens.components.glasskit
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.moontrade.ui.theme.Violet600

@Composable
fun TagChip(
    text: String,
    modifier: Modifier = Modifier
) {
    val bg = Brush.horizontalGradient(
        listOf(
            Violet600.copy(alpha = 0.6f),
            Violet600.copy(alpha = 0.9f),
            Violet600.copy(alpha = 1.2f)
        )
    )
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(30))
            .background(bg)
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = Color.White,
            maxLines = 1
        )
    }
}
