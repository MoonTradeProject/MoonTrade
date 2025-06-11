package com.example.moontrade.ui.screens.components.tournament

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isJoined) Color(0xFFD8F8D8) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(4.dp))
            Text(subtitle, style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray))
            Spacer(Modifier.height(12.dp))

            Button(
                onClick = onAction,
                enabled = !isJoined,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(if (isJoined) "Joined" else actionText)
            }
        }
    }
}

