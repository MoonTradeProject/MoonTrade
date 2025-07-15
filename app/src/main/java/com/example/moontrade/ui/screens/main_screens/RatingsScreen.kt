package com.example.moontrade.ui.screens.main_screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moontrade.navigation.NavRoutes
import com.example.moontrade.ui.screens.components.PlayerCard
import com.example.moontrade.ui.screens.components.bars.TopBar
import com.example.moontrade.viewmodels.LeaderboardViewModel
import com.example.moontrade.viewmodels.SelectedPlayerViewModel
import kotlinx.coroutines.delay

@Composable
fun RatingsScreen(
    navController: NavController,
    viewModel: LeaderboardViewModel,
    selectedPlayerViewModel: SelectedPlayerViewModel
) {
    val entries by viewModel.entries.collectAsState()

    LaunchedEffect(Unit) {
        while (true) {
            viewModel.loadLeaderboard()
            delay(10_000)
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "Leaderboard",
                showBack = true,
                onBack = { navController.popBackStack() }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Top Players", style = MaterialTheme.typography.headlineMedium)
            }

            itemsIndexed(entries) { _, entry ->
                PlayerCard(entry = entry) {
                    selectedPlayerViewModel.set(entry)
                    navController.navigate(NavRoutes.PLAYER_PROFILE)
                }
            }
            }
        }
    }

