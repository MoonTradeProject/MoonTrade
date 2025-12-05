package com.example.moontrade.ui.screens.main_screens.home_sections

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moontrade.model.Mode
import com.example.moontrade.ui.theme.Violet200
import com.example.moontrade.ui.theme.Violet300
import com.example.moontrade.ui.theme.Violet600

data class SelectableMode(
    val mode: Mode,
    val label: String
)

@Composable
fun ModeSelector(
    selected: SelectableMode,
    items: List<SelectableMode>,
    onSelect: (SelectableMode) -> Unit,
    modifier: Modifier = Modifier
) {
    if (items.isEmpty()) return

    val scroll = rememberScrollState()
    val chipShape = RoundedCornerShape(20.dp)
    val cs = MaterialTheme.colorScheme

    Row(
        modifier = modifier
            .horizontalScroll(scroll)
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            val isSelected = remember(selected, item) { item == selected }

            val chipBg: Brush? = if (isSelected) {
                Brush.horizontalGradient(
                    listOf(
                        Violet600,
                        Violet300.copy(alpha = 0.9f),
                        Violet600
                    )
                )
            } else null

            val borderColor =
                if (isSelected) Color.Transparent
                else Violet200.copy(alpha = 0.4f)

            val textColor =
                if (isSelected) Color.White
                else cs.onSurface.copy(alpha = 0.9f)

            var chipModifier = Modifier
                .heightIn(min = 40.dp)
                .clip(chipShape)

            chipModifier = if (chipBg != null) {
                chipModifier.background(chipBg)
            } else {
                chipModifier.background(Color.Transparent)
            }

            chipModifier = chipModifier
                .border(1.dp, borderColor, chipShape)
                .clickable { onSelect(item) }
                .padding(horizontal = 14.dp, vertical = 9.dp)

            Box(
                modifier = chipModifier,
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.label,
                    color = textColor,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
