package com.example.moontrade.ui.screens.main_screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.moontrade.ui.screens.components.bars.TopBar
import com.example.moontrade.viewmodels.ProfileViewModel

@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val nickname by viewModel.nickname.collectAsState()
    val selectedTags by viewModel.selectedTags.collectAsState()
    val availableTags = viewModel.availableTags

    var tempNickname by remember { mutableStateOf(nickname) }
    var tempTags by remember { mutableStateOf(selectedTags.toMutableList()) }

    Scaffold(
        topBar = {
            TopBar(
                title = "Edit Profile",
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = tempNickname,
                onValueChange = { tempNickname = it },
                label = { Text("Nickname") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Visible Tags", style = MaterialTheme.typography.titleMedium)

            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                availableTags.forEach { tag ->
                    FilterChip(
                        selected = tag in tempTags,
                        onClick = {
                            tempTags = (if (tag in tempTags) {
                                tempTags - tag
                            } else {
                                tempTags + tag
                            }) as MutableList<String>
                        },
                        label = { Text(tag) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.updateNickname(tempNickname)
                    viewModel.updateSelectedTags(tempTags)
                    navController.popBackStack()
                }
            ) {
                Text("Save Changes")
            }
        }
    }
}
