package com.example.moontrade.ui.screens.main_screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.moontrade.model.Mode
import com.example.moontrade.model.WebSocketStatus
import com.example.moontrade.viewmodels.BalanceViewModel
import com.example.moontrade.viewmodels.TournamentsViewModel

data class SelectableMode(
    val mode: Mode,
    val label: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    balanceViewModel: BalanceViewModel,
    tournamentsViewModel: TournamentsViewModel = hiltViewModel()
) {
    val mode by balanceViewModel.mode.collectAsState()
    val balance by balanceViewModel.balance.collectAsState()
    val status by balanceViewModel.status.collectAsState()
    val tournaments by tournamentsViewModel.tournaments.collectAsState()

    val selectableModes = listOf(
        SelectableMode(Mode.Main, "Main")
    ) + tournaments.filter { it.isJoined }.map {
        SelectableMode(Mode.Tournament(it.id.toString()), it.name)
    }

    var selected by remember(mode, selectableModes) {
        mutableStateOf(selectableModes.find { it.mode == mode } ?: selectableModes.first())
    }

    LaunchedEffect(Unit) {
        balanceViewModel.connect()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        var expanded by remember { mutableStateOf(false) }

                        Box {
                            TextButton(onClick = { expanded = true }) {
                                Text(selected.label)
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                selectableModes.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(item.label) },
                                        onClick = {
                                            selected = item
                                            expanded = false
                                            balanceViewModel.changeMode(item.mode)
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.weight(1f))

                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(
                                    color = when (status) {
                                        is WebSocketStatus.Connected -> Color.Green
                                        is WebSocketStatus.Connecting -> Color.Yellow
                                        is WebSocketStatus.Error -> Color.Red
                                        WebSocketStatus.Idle -> Color.Gray
                                    },
                                    shape = MaterialTheme.shapes.extraSmall
                                )
                        )

                        Spacer(Modifier.width(8.dp))
                        Text(text = balance, style = MaterialTheme.typography.titleMedium)
                    }
                }
            )
        },

    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Portfolio Balance", style = MaterialTheme.typography.titleLarge)
                        Spacer(Modifier.height(8.dp))
                        Text(balance, style = MaterialTheme.typography.headlineSmall)
                        Text("ROI: +3.4%", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            item {
                Text("Top 5 Players", style = MaterialTheme.typography.titleLarge)
            }

            val topPlayers = listOf(
                "TraderA" to "+25.3%",
                "TraderB" to "+18.4%",
                "TraderC" to "+16.7%",
                "TraderD" to "+12.1%",
                "TraderE" to "+10.5%"
            )

            items(topPlayers) { (name, roi) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("player_profile/${name}")
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(name, style = MaterialTheme.typography.bodyLarge)
                            Text("ROI: $roi", style = MaterialTheme.typography.bodyMedium)
                        }
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "View")
                    }
                }
            }

            item {
                Text("Your Portfolio", style = MaterialTheme.typography.titleLarge)
            }

            item { AssetCardStub("BTC", "0.42 BTC") }
            item { AssetCardStub("ETH", "3.1 ETH") }
            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun AssetCardStub(label: String, value: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(label, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
