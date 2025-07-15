package com.example.moontrade.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moontrade.viewmodels.SelectedPlayerViewModel
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PlayerProfileScreen(
    navController: NavController,
    selectedPlayerViewModel: SelectedPlayerViewModel
) {
    val player by selectedPlayerViewModel.selected.collectAsState()

    if (player == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No player selected.")
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(player!!.username) },
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
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("ROI: +%.1f%%".format(player!!.roi), style = MaterialTheme.typography.titleLarge)
            Text("Rank: ${player!!.rank}", style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(16.dp))
            Text(player!!.description ?: "No description.", style = MaterialTheme.typography.bodyMedium)

            Spacer(Modifier.height(24.dp))
            Text("Tags", style = MaterialTheme.typography.titleMedium)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                player!!.tags.forEach { tag ->
                    AssistChip(onClick = {}, label = { Text(tag) })
                }
            }

            Spacer(Modifier.height(24.dp))
            Text("Achievements", style = MaterialTheme.typography.titleMedium)
            player!!.achievements.forEach {
                Text("• $it", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
