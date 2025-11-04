package com.example.moontrade.ui.screens.components.bars

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.moontrade.ui.theme.*

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

private val bottomNavItems = listOf(
    BottomNavItem("home", "Home", Icons.Default.Home),
    BottomNavItem("markets", "Markets", Icons.Default.CheckCircle),
    BottomNavItem("ratings", "Ratings", Icons.Default.AccountCircle),
    BottomNavItem("tournaments", "Tournaments", Icons.Default.Star)
)

@Composable
fun BottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val currentRoot = currentRoute?.substringBefore('/')

    NavigationBar(
        containerColor = Ink950,  // почти чёрный фон бара
        tonalElevation = 0.dp     // убираем “туман”
    ) {
        bottomNavItems.forEach { item ->
            val selected = currentRoot == item.route

            NavigationBarItem(
                selected = selected,
                icon = { Icon(item.icon, null) },
                label = { Text(item.label) },
                onClick = {
                    if (!selected) {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Violet800,           // подсветка выбранного пункта
                    selectedIconColor = Violet200,        // фиолетовая иконка
                    selectedTextColor = Violet200,        // фиолетовая подпись
                    unselectedIconColor = TextSecondaryDark, // серые невыбранные
                    unselectedTextColor = TextSecondaryDark
                )
            )
        }
    }
}
