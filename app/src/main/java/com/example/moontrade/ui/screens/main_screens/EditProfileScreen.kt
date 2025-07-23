package com.example.moontrade.ui.screens.main_screens

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.moontrade.R
import com.example.moontrade.ui.screens.components.bars.TopBar
import com.example.moontrade.viewmodels.ProfileViewModel
import com.google.accompanist.flowlayout.FlowRow

@SuppressLint("MutableCollectionMutableState")
@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    /* ---- state from ViewModel ------------------------------------------------ */

    val nickname      by viewModel.nickname.collectAsState()
    val selectedTags  by viewModel.selectedTags.collectAsState()
    val avatarId      by viewModel.avatarId.collectAsState()
    val avatarUrl     by viewModel.avatarUrl.collectAsState()

    val availableTags = viewModel.availableTags
    val context       = LocalContext.current

    /* ---- local editable copies --------------------------------------------- */

    var tempNickname  by remember { mutableStateOf(nickname) }
    var tempTags      by remember { mutableStateOf(selectedTags.toMutableList()) }
    var tempAvatarId  by remember { mutableIntStateOf(avatarId) }
    var description   by remember { mutableStateOf("") }

    /* ---- image picker ------------------------------------------------------- */

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.uploadAvatarFromUri(context, it) {
                tempAvatarId = -1                  // custom avatar selected
            }
        }
    }

    /* ---- UI ---------------------------------------------------------------- */

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
            /* Nickname -------------------------------------------------------- */
            OutlinedTextField(
                value = tempNickname,
                onValueChange = { tempNickname = it },
                label = { Text("Nickname") },
                modifier = Modifier.fillMaxWidth()
            )

            /* Description ----------------------------------------------------- */
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            /* Tags ------------------------------------------------------------ */
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

            /* Upload avatar --------------------------------------------------- */
            Text("Upload Avatar", style = MaterialTheme.typography.titleMedium)

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = { launcher.launch("image/*") }) {
                    Text("Upload from Gallery")
                }

                if (avatarUrl != null) {
                    Image(
                        painter = rememberAsyncImagePainter(avatarUrl),
                        contentDescription = "Uploaded avatar",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    )
                }
            }

            /* Choose built-in avatar ----------------------------------------- */
            Text("Choose Avatar", style = MaterialTheme.typography.titleMedium)

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.height(240.dp)
            ) {
                /* custom uploaded avatar */
                if (avatarUrl != null) {
                    item {
                        val selected = tempAvatarId == -1
                        Box(
                            modifier = Modifier
                                .padding(6.dp)
                                .clip(CircleShape)
                                .background(
                                    if (selected)
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                    else Color.Transparent
                                )
                                .clickable { tempAvatarId = -1 }
                                .padding(4.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(avatarUrl),
                                contentDescription = "Custom Avatar",
                                modifier = Modifier.size(64.dp)
                            )
                        }
                    }
                }

                /* built-in avatars */
                items(10) { id ->
                    val selected = id == tempAvatarId
                    Box(
                        modifier = Modifier
                            .padding(6.dp)
                            .clip(CircleShape)
                            .background(
                                if (selected)
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                else Color.Transparent
                            )
                            .clickable { tempAvatarId = id
                                viewModel.updateAvatarId(id, description) }
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

            /* Save button ------------------------------------------------------ */
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.updateNickname(tempNickname)
                    viewModel.updateSelectedTags(tempTags)

                    if (tempAvatarId == -1) {
                        viewModel.saveProfile(description)
                    }

                    navController.popBackStack()
                }
            ) {
                Text("Save Changes")
            }

        }
    }
}

/* helper to map built-in avatar id → drawable ------------------------------ */
@Composable
fun avatarResIdFrom(id: Int): Int = when (id) {
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
