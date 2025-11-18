package com.example.moontrade.ui.screens.components.bars

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ShowChart
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

private val bottomNavItems = listOf(
    BottomNavItem("home", "Home", Icons.Outlined.Home),
    BottomNavItem("markets", "Market", Icons.AutoMirrored.Outlined.ShowChart),
    BottomNavItem("ratings", "Rating", Icons.Outlined.Star),
    BottomNavItem("tournaments", "Events", Icons.Outlined.EmojiEvents),
    BottomNavItem("profile", "Profile", Icons.Outlined.Person)
)

@Composable
fun BottomBar(navController: NavController) {
    val cs = MaterialTheme.colorScheme
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val currentRoot = currentRoute?.substringBefore('/')

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        containerColor = cs.background,
        tonalElevation = 0.dp,
        windowInsets = WindowInsets(0)
    ) {
        bottomNavItems.forEach { item ->

            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                containerColor = cs.background,
                tonalElevation = 0.dp,
                windowInsets = WindowInsets(0)
            ) {
                bottomNavItems.forEach { item ->
                    val selected = currentRoot == item.route

                    NavigationBarItem(
                        selected = selected,
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                maxLines = 1
                            )
                        },
                        onClick = {
                            if (!selected) navController.navigate(item.route) { launchSingleTop = true }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = cs.primary,
                            selectedIconColor = cs.onPrimary,
                            selectedTextColor = cs.onPrimary,
                            unselectedIconColor = cs.onSurfaceVariant,
                            unselectedTextColor = cs.onSurfaceVariant
                        ),
                        modifier = Modifier.padding(horizontal = 0.dp)
                    )
                }
            }

        }
    }
}
