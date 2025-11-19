package com.example.moontrade.ui.screens.main_screens.market_details_sub_screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DropdownMenuBox(
    selected: String,
    options: List<String>,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var boxWidth by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
            .onGloballyPositioned{ coordinates ->
                boxWidth = coordinates.size.width // px
            }
            .clickable { expanded = true }

            .padding(vertical = 12.dp),

        contentAlignment = Alignment.CenterStart
    ) {

        Text(
            text = selected,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "dropdown arrow",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(25.dp)
                .padding(end = 4.dp)
                .rotate(if (expanded) 180f else 0f)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.DarkGray.copy(alpha = 0.4f))
                .width(with(LocalDensity.current) { boxWidth.toDp() })
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            option,
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            color = if (option == selected) Color(0xFF47299B) else Color.White,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    onClick = {
                        expanded = false
                        onSelected(option)
                    },

                    )
            }
        }
    }
}