package com.example.moontrade.ui.screens.main_screens.home_sub_screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.moontrade.ui.screens.components.bars.TopBar
import com.example.moontrade.ui.theme.extended
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle


data class SelectableMode(val mode: com.example.moontrade.model.Mode, val label: String)

@Composable
fun HomeTopBar(
    selected: SelectableMode,
    items: List<SelectableMode>,
    onSelect: (SelectableMode) -> Unit,
    onOpenSettings: () -> Unit
) {
    val ex = MaterialTheme.extended
    val cs = MaterialTheme.colorScheme

    TopBar(
        title = null,
        showBack = false,
        navigationContent = {
            var expanded by remember { mutableStateOf(false) }
            Box {
                TextButton(onClick = { expanded = true }) {
                    Text(selected.label, style = MaterialTheme.typography.titleMedium, color = cs.onSurface)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(ex.glassSurface)
                ) {
                    items.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item.label, color = cs.onSurface) },
                            onClick = { onSelect(item); expanded = false }
                        )
                    }
                }
            }
        },
        centerContent = {
            val label = buildAnnotatedString {
                withStyle(SpanStyle(brush = ex.gradientAccent)) { append("MOONTRADE") }
            }
            Text(text = label, style = MaterialTheme.typography.titleLarge, fontSize = 20.sp, maxLines = 1)
        },
        actions = {
            IconButton(onClick = onOpenSettings) { Icon(Icons.Default.Settings, contentDescription = "Settings") }
        }
    )
}
