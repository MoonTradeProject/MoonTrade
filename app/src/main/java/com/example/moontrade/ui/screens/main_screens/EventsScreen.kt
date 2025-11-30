package com.example.moontrade.ui.screens.main_screens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.moontrade.data.dto.TournamentDto
import com.example.moontrade.data.enums.TournamentKind
import com.example.moontrade.data.enums.TournamentPaymentMethod
import com.example.moontrade.data.enums.TournamentStatus
import com.example.moontrade.ui.screens.components.bars.SectionHeader
import com.example.moontrade.ui.screens.components.bars.TopBar
import com.example.moontrade.ui.screens.components.tournament.JoinTournamentDialog
import com.example.moontrade.ui.screens.components.tournament.TournamentCard
import com.example.moontrade.viewmodels.TournamentsViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlinx.coroutines.delay

@Composable
fun TournamentsScreen(
    navController: NavController,
    viewModel: TournamentsViewModel = hiltViewModel()
) {
    val tournaments by viewModel.tournaments.collectAsState()
    var selectedTournamentId by remember { mutableStateOf<String?>(null) }
    var selectedTournamentName by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    // подгрузка турниров
    LaunchedEffect(Unit) {
        while (true) {
            viewModel.loadTournaments()
            delay(30_000)
        }
    }

    TournamentsScreenContent(
        tournaments = tournaments,
        scrollState = scrollState,
        selectedTournamentName = selectedTournamentName,
        onTournamentClick = { tournament ->
            selectedTournamentId = tournament.id.toString()
            selectedTournamentName = tournament.name
        },
        onDismissDialog = {
            selectedTournamentId = null
            selectedTournamentName = null
        },
        onConfirmJoin = { method ->
            selectedTournamentId?.let { id ->
                viewModel.joinTournament(
                    tournamentId = UUID.fromString(id).toString(),
                    method = method
                )
            }
            selectedTournamentId = null
            selectedTournamentName = null
        }
    )
}


@Composable
private fun TournamentsScreenContent(
    tournaments: List<TournamentDto>,
    scrollState: ScrollState,
    selectedTournamentName: String?,
    onTournamentClick: (TournamentDto) -> Unit,
    onDismissDialog: () -> Unit,
    onConfirmJoin: (TournamentPaymentMethod) -> Unit
) {
    Scaffold(
        topBar = { TopBar(title = "Tournaments", showBack = false) },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            SectionHeader(
                title = "Available Tournaments"
            )

            Spacer(Modifier.height(4.dp))

            tournaments.forEach { tournament ->
                val formattedDate = remember(tournament.startTime) {
                    tournament.startTime.format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    )
                }
                val subtitle = "${tournament.kind} | Starts: $formattedDate"

                TournamentCard(
                    title = tournament.name,
                    subtitle = subtitle,
                    isJoined = tournament.isJoined,
                    actionText = "Join",
                    onAction = { onTournamentClick(tournament) }
                )

                Spacer(Modifier.height(12.dp))
            }
        }
    }

    if (selectedTournamentName != null) {
        JoinTournamentDialog(
            tournamentName = selectedTournamentName,
            onDismiss = onDismissDialog,
            onConfirm = onConfirmJoin
        )
    }
}


// обычное превью
@Preview(showBackground = true, widthDp = 380, heightDp = 800)
@Composable
fun TournamentsScreen_Preview_Normal() {
    val fakeTournaments = listOf(
        TournamentDto(
            id = UUID.randomUUID(),
            name = "Daily BTC Challenge",
            kind = TournamentKind.entries.first(),
            startTime = LocalDateTime.now(),
            endTime = LocalDateTime.now().plusDays(1),
            status = TournamentStatus.Active,
            isJoined = false
        ),
        TournamentDto(
            id = UUID.randomUUID(),
            name = "Weekend ETH Cup",
            kind = TournamentKind.entries.first(),
            startTime = LocalDateTime.now().plusDays(1),
            endTime = LocalDateTime.now().plusDays(2),
            status = TournamentStatus.Active,
            isJoined = true
        )
    )

    MaterialTheme {
        TournamentsScreenContent(
            tournaments = fakeTournaments,
            scrollState = rememberScrollState(),
            selectedTournamentName = null,
            onTournamentClick = {},
            onDismissDialog = {},
            onConfirmJoin = {}
        )
    }
}

// превью с увеличенным текстом
@Preview(
    showBackground = true,
    widthDp = 380,
    heightDp = 800,
    fontScale = 1.4f
)
@Composable
fun TournamentsScreen_Preview_BigFont() {
    TournamentsScreen_Preview_Normal()
}

// превью на узком устройстве
@Preview(
    showBackground = true,
    device = "spec:width=320dp,height=700dp,dpi=320"
)
@Composable
fun TournamentsScreen_Preview_SmallDevice() {
    TournamentsScreen_Preview_Normal()
}
