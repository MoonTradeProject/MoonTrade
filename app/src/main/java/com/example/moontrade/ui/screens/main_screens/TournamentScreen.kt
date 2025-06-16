package com.example.moontrade.ui.screens.main_screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.moontrade.ui.screens.components.tournament.JoinTournamentDialog
import com.example.moontrade.ui.screens.components.tournament.TournamentCard
import com.example.moontrade.viewmodels.TournamentsViewModel
import com.example.moontrade.ui.screens.components.bars.BottomBar
import com.example.moontrade.ui.screens.components.bars.TopBar
import java.time.format.DateTimeFormatter
import java.util.UUID

@Composable
fun TournamentsScreen(
    navController: NavController,
    viewModel: TournamentsViewModel = hiltViewModel()
) {
    val tournaments by viewModel.tournaments.collectAsState()
    var selectedTournamentId by remember { mutableStateOf<String?>(null) }
    var selectedTournamentName by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = { TopBar(title = "Tournaments", showBack = false) },

    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Available Tournaments", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))

            tournaments.forEach { tournament ->
                val formattedDate = remember(tournament.startTime) {
                    tournament.startTime?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) ?: "unknown"
                }
                val subtitle = "${tournament.kind} | Starts: $formattedDate"

                TournamentCard(
                    title = tournament.name,
                    subtitle = subtitle,
                    isJoined = tournament.isJoined,
                    actionText = "Join",
                    onAction = {
                        selectedTournamentId = tournament.id.toString()
                        selectedTournamentName = tournament.name
                    }
                )
                Spacer(Modifier.height(12.dp))
            }
        }
    }

    // Show join dialog
    if (selectedTournamentId != null && selectedTournamentName != null) {
        JoinTournamentDialog(
            tournamentName = selectedTournamentName!!,
            onDismiss = {
                selectedTournamentId = null
                selectedTournamentName = null
            },
            onConfirm = { method ->
                viewModel.joinTournament(
                    tournamentId = UUID.fromString(selectedTournamentId!!).toString(),
                    method = method
                )
                selectedTournamentId = null
                selectedTournamentName = null
            }
        )
    }
}
