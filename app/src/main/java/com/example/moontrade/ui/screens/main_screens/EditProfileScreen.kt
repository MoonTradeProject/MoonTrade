package com.example.moontrade.ui.screens.main_screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.moontrade.R
import com.example.moontrade.ui.screens.components.bars.TopBar
import com.example.moontrade.viewmodels.ProfileViewModel

@SuppressLint("MutableCollectionMutableState")
@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel
) {
    val nickname by viewModel.nickname.collectAsState()
    val selectedTags by viewModel.selectedTags.collectAsState()
    val avatarId by viewModel.avatarId.collectAsState()
    val availableTags = viewModel.availableTags

    val avatarCount = 10

    var tempNickname by remember { mutableStateOf(nickname) }
    var tempTags by remember { mutableStateOf(selectedTags.toMutableList()) }
    var tempAvatarId by remember { mutableIntStateOf(avatarId) }

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

            Text("Choose Avatar", style = MaterialTheme.typography.titleMedium)

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.height(240.dp)
            ) {
                items(avatarCount) { id ->
                    val selected = id == tempAvatarId
                    Box(
                        modifier = Modifier
                            .padding(6.dp)
                            .clip(CircleShape)
                            .background(if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else Color.Transparent)
                            .clickable { tempAvatarId = id }
                            .padding(4.dp)
                    ) {
                        Image(
                            painter = painterResource(id = avatarResIdFrom(id)),
                            contentDescription = "Avatar $id",
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.updateNickname(tempNickname)
                    viewModel.updateSelectedTags(tempTags)
                    viewModel.updateAvatarId(tempAvatarId)
                    navController.popBackStack()
                }
            ) {
                Text("Save Changes")
            }
        }
    }
}

@Composable
fun avatarResIdFrom(id: Int): Int {
    return when (id) {
        0 -> R.drawable.avatar_0
        1 -> R.drawable.avatar_1
        2 -> R.drawable.avatar_2
        3 -> R.drawable.avatar_3
        4 -> R.drawable.avatar_4
        5 -> R.drawable.avatar_5
        6 -> R.drawable.avatar_6
        7 -> R.drawable.avatar_7
        8 -> R.drawable.avatar_8

        else -> R.drawable.img
    }
}
