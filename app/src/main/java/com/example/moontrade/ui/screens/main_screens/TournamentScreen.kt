package com.example.moontrade.ui.screens.main_screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moontrade.ui.screens.components.bars.BottomBar
import com.example.moontrade.ui.screens.components.bars.TopBar
import com.example.moontrade.ui.screens.components.tournament.JoinTournamentDialog
import com.example.moontrade.ui.screens.components.tournament.TournamentCard

@Composable
fun TournamentsScreen(navController: NavController) {
    var showJoinDialog by remember { mutableStateOf<String?>(null) }

    val myTournaments = listOf("3-Month Pro League", "2-Week Blitz")
    val availableTournaments = listOf("6-Month Challenge", "1-Month Sprint")

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopBar(
                title = "Tournaments",
                showBack = false
            )
        },
        bottomBar = {
            BottomBar(navController)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("My Tournaments", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))

            myTournaments.forEach {
                TournamentCard(
                    title = it,
                    subtitle = "ROI: +12.4%",
                    actionText = "View Leaderboard",
                    onAction = { /* TODO */ }
                )
                Spacer(Modifier.height(12.dp))
            }

            Spacer(Modifier.height(24.dp))

            Text("Available Tournaments", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))

            availableTournaments.forEach {
                TournamentCard(
                    title = it,
                    subtitle = "Entry: 1000 Stack",
                    actionText = "Join",
                    onAction = { showJoinDialog = it }
                )
                Spacer(Modifier.height(12.dp))
            }
        }
    }

    showJoinDialog?.let { tournamentName ->
        JoinTournamentDialog(
            tournamentName = tournamentName,
            onDismiss = { showJoinDialog = null },
            onConfirm = { method ->
                // TODO: handle method
                println("Chosen method: $method")
                showJoinDialog = null
            }
        )
    }
}
