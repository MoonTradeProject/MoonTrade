package com.example.moontrade.ui.screens.main_screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moontrade.navigation.NavRoutes
import com.example.moontrade.ui.screens.components.bars.TopBar
import com.example.moontrade.ui.screens.components.PlayerCard

@Composable
fun RatingsScreen(navController: NavController) {
    val topPlayers = listOf(
        Triple("TraderOne", "+25.3%", Icons.Default.ThumbUp),
        Triple("TraderTwo", "+18.4%", Icons.Default.Star),
        Triple("TraderThree", "+16.7%", Icons.Default.Check),
        Triple("TraderFour", "+14.1%", null),
        Triple("TraderFive", "+12.0%", null),
        Triple("TraderSix", "+10.2%", null),
        Triple("TraderSeven", "+9.7%", null),
    )

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
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Top Players", style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(4.dp))
                Text("View leaderboards by league and season.")
            }

            itemsIndexed(topPlayers) { index, (playerId, roi, icon) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("player_profile/$playerId")
                        },
                    colors = if (index == 0) CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    else CardDefaults.cardColors()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (icon != null) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = "Trophy",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                            }
                            Column {
                                Text(playerId, style = MaterialTheme.typography.bodyLarge)
                                Text("ROI: $roi", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "View Profile"
                        )
                    }
                }
            }
        }
    }
}