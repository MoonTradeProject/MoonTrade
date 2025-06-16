package com.example.moontrade.ui.screens.main_screens

package com.example.moontrade.ui.screens.main_screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moontrade.navigation.NavRoutes
import com.example.moontrade.ui.screens.components.bars.TopBar

@Composable
fun SettingsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopBar(
                title = "Settings",
                showBack = true,
                onBack = { navController.popBackStack() }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Text("App Settings", style = MaterialTheme.typography.titleLarge)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* TODO: Theme switch */ }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.DarkMode, contentDescription = null)
                Spacer(Modifier.width(12.dp))
                Text("Dark Theme")
                Spacer(Modifier.weight(1f))
                Switch(checked = true, onCheckedChange = { /* TODO: switch */ })
            }

            Divider()

            Text("Profile", style = MaterialTheme.typography.titleLarge)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(NavRoutes.PROFILE_EDIT)
                    }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.AccountCircle, contentDescription = null)
                Spacer(Modifier.width(12.dp))
                Text("Edit Profile")
            }

            Divider()

            Text("Security", style = MaterialTheme.typography.titleLarge)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        // TODO: logout()
                    }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null)
                Spacer(Modifier.width(12.dp))
                Text("Log out")
            }
        }
    }
}
