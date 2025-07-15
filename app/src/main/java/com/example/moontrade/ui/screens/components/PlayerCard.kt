package com.example.moontrade.ui.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moontrade.model.LeaderboardEntry

@Composable
fun PlayerCard(
    entry: LeaderboardEntry,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(entry.username, style = MaterialTheme.typography.titleMedium)
                Text("ROI: +%.1f%%".format(entry.roi), style = MaterialTheme.typography.bodyMedium)
                if (!entry.description.isNullOrBlank()) {
                    Text(entry.description, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
