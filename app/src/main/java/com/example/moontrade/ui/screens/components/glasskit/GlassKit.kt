package com.example.moontrade.ui.screens.components.glasskit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.moontrade.ui.theme.extended

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    corner: Dp = 22.dp,
    overlay: Brush? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val ex = MaterialTheme.extended
    val shape = RoundedCornerShape(corner)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(16.dp, shape, clip = false)
            .clip(shape)
            .background(ex.glassCard)
            .then(if (overlay != null) Modifier.background(overlay, shape) else Modifier)
            .border(
                1.dp,
                Brush.linearGradient(
                    listOf(
                        Color.White.copy(alpha = 0.18f),
                        Color.White.copy(alpha = 0.06f)
                    )
                ),
                shape
            ),
        color = Color.Transparent,
        contentColor = cs.onSurface,
        shape = shape
    ) {
        Column(Modifier.padding(18.dp), content = content)
    }
}

@Composable
fun GradientText(text: String, brush: Brush, style: TextStyle) {
    Text(
        text = buildAnnotatedString { withStyle(SpanStyle(brush = brush)) { append(text) } },
        style = style
    )
}

@Composable
fun Pill(label: String, brush: Brush, leadingDot: Boolean = false) {
    Surface(
        color = Color.Transparent,
        shape = RoundedCornerShape(999.dp),
        modifier = Modifier
            .height(28.dp)
            .border(1.dp, brush, RoundedCornerShape(999.dp))
            .background(brush, RoundedCornerShape(999.dp))
            .padding(horizontal = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (leadingDot) {
                Box(
                    Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(.85f))
                )
                Spacer(Modifier.width(8.dp))
            }
            Text(label, color = Color.White, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
fun AvatarWithRing(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .background(MaterialTheme.extended.gradientAvatar, CircleShape)
            .padding(3.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface, CircleShape)
    ) { content() }
}
