package com.example.moontrade.ui.screens.components.bars

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.moontrade.R

data class BottomNavItem(
    val route: String,
    val label: String,
    val iconRes: Int
)

private val bottomNavItems = listOf(
    BottomNavItem("home", "Home", R.drawable.ic_home),
    BottomNavItem("markets", "Market", R.drawable.ic_market),
    BottomNavItem("ratings", "Rating", R.drawable.ic_leader),
    BottomNavItem("tournaments", "Events", R.drawable.ic_events),
    BottomNavItem("profile", "Profile", R.drawable.ic_profile)
)

@Composable
fun BottomBar(navController: NavController) {
    val cs = MaterialTheme.colorScheme

    val isDarkTheme = cs.background.luminance() < 0.5f
    val selectedColor = if (isDarkTheme) com.example.moontrade.ui.theme.Violet50 else com.example.moontrade.ui.theme.Bluer

    val labelSelectedColor = selectedColor
    val labelUnselectedColor = labelSelectedColor.copy(alpha = 0.6f)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val currentRoot = currentRoute?.substringBefore('/')

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 2.dp),
        containerColor = cs.background,
        tonalElevation = 20.dp,
        windowInsets = WindowInsets(0)
    ) {
        bottomNavItems.forEach { item ->
            val selected = currentRoot == item.route

            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                        }
                    }
                },
                icon = {
                    Image(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = item.label,
                        modifier = Modifier.size(36.dp),
                        colorFilter = if (selected)
                            ColorFilter.tint(selectedColor)
                        else
                            null
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (selected) labelSelectedColor else labelUnselectedColor
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = Color.Unspecified,
                    unselectedIconColor = Color.Unspecified,
                    selectedTextColor = labelSelectedColor,
                    unselectedTextColor = labelUnselectedColor
                ),
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}
