package com.example.moontrade.ui.screens.main_screens.home_sub_screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.moontrade.model.Mode
import com.example.moontrade.ui.theme.Violet600
import com.example.moontrade.ui.theme.extended

data class SelectableMode(
    val mode: Mode,
    val label: String
)

@Composable
fun ModeSelector(
    selected: SelectableMode,
    items: List<SelectableMode>,
    onSelect: (SelectableMode) -> Unit
) {
    val ex = MaterialTheme.extended
    val cs = MaterialTheme.colorScheme

    var expanded by remember { mutableStateOf(false) }

    Box {
        TextButton(onClick = { expanded = true }) {
            Text(
                text = selected.label,
                style = MaterialTheme.typography.titleMedium,
                color = Violet600
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(ex.glassSurface)
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.label, color = cs.onSurface) },
                    onClick = {
                        onSelect(item)
                        expanded = false
                    }
                )
            }
        }
    }
}
