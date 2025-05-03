package com.example.moontrade.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.FlowRow

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PlayerProfeleScreen(
    navController: NavController,
    playerId: String,
    isCurrentUser: Boolean = false
) {
    var description by remember { mutableStateOf("I'm a legendary trader dominating tournaments.") }
    val achievements = listOf("🏆 Season Champion", "📈 Top 1% ROI", "⚔️ PvP Master")
    val tags = listOf("Risk-Taker", "Long-Term", "Crypto-Only")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Player Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = playerId.take(1).uppercase(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(Modifier.height(12.dp))
            Text(playerId, style = MaterialTheme.typography.headlineSmall)
            Text("ROI: +23.8%", style = MaterialTheme.typography.bodyMedium)

            Spacer(Modifier.height(24.dp))

            Text("About Me", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            if (isCurrentUser) {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Write something about yourself...") }
                )
            } else {
                Text(description, style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(Modifier.height(24.dp))

            Text("Achievements", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            achievements.forEach {
                Text("• $it", style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(Modifier.height(24.dp))

            Text("Tags", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                tags.forEach {
                    AssistChip(
                        onClick = {},
                        label = { Text(it) }
                    )
                }
            }

            Spacer(Modifier.height(64.dp))
        }
    }
}
