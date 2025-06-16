package com.example.moontrade.ui.screens.components.tournament

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TournamentCard(
    title: String,
    subtitle: String,
    isJoined: Boolean,
    actionText: String,
    onAction: () -> Unit
) {
    val shape = RoundedCornerShape(12.dp)
    val borderColor = if (isJoined) MaterialTheme.colorScheme.primary else Color.Transparent
    val cardAlpha = if (isJoined) 0.85f else 1f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, borderColor, shape)
            .alpha(cardAlpha),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                Text(subtitle, style = MaterialTheme.typography.bodyMedium)
            }

            if (isJoined) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Joined",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Joined", color = MaterialTheme.colorScheme.primary)
                }
            } else {
                Button(onClick = onAction) {
                    Text(actionText)
                }
            }
        }
    }
}
