package com.example.moontrade.ui.screens.main_screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(start = 4.dp, end = 16.dp, top = 4.dp, bottom = 8.dp)
                ) {
                    Box(
                        Modifier
                            .width(4.dp)
                            .height(22.dp)
                            .padding(start = 12.dp)
                            .background(
                                brush = Brush.verticalGradient(
                                    listOf(
                                        Color(0xFF8D6BFF),
                                        Color(0xFFB38AFF)
                                    )
                                ),
                                shape = RoundedCornerShape(100)
                            )
                    )

                    Spacer(Modifier.width(12.dp))

                    Text(
                        text = "Top Players",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                    )
                }
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
