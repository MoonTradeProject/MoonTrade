package com.example.moontrade.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.moontrade.R
import com.example.moontrade.navigation.NavRoutes
import com.example.moontrade.ui.screens.components.bars.TopBar
import com.example.moontrade.ui.screens.components.glasskit.GlassCard
import com.example.moontrade.viewmodels.ProfileViewModel
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun UserProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel
) {
    val nickname by profileViewModel.nickname.collectAsState()
    val selectedTags by profileViewModel.selectedTags.collectAsState()
    val avatarId by profileViewModel.avatarId.collectAsState()
    val avatarUrl by profileViewModel.avatarUrl.collectAsState()

    val cs = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            TopBar(
                title = "Profile",
                showBack = true,
                onBack = { navController.popBackStack() },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(NavRoutes.PROFILE_EDIT)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_edit),
                            contentDescription = "Edit",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(42.dp)
                        )
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(cs.background)
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    corner = 28.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .clip(CircleShape)
                        ) {
                            if (!avatarUrl.isNullOrEmpty()) {
                                AsyncImage(
                                    model = avatarUrl,
                                    contentDescription = "avatar",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = resolveDefaultAvatar(avatarId)),
                                    contentDescription = "avatar",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        // Nickname
                        Text(
                            text = nickname,
                            style = MaterialTheme.typography.titleLarge,
                            color = cs.onSurface
                        )

                        Spacer(Modifier.height(12.dp))

                        // Tags
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            selectedTags.forEach { tag ->
                                AssistChip(
                                    onClick = {},
                                    label = { Text(tag) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun resolveDefaultAvatar(id: Int): Int = when (id) {
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
