@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.moontrade.ui.screens.main_screens

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.moontrade.R
import com.example.moontrade.navigation.NavRoutes
import com.example.moontrade.ui.screens.components.bars.SectionHeader
import com.example.moontrade.ui.screens.components.bars.TopBar
import com.example.moontrade.ui.screens.components.buttons.PrimaryGradientButton
import com.example.moontrade.ui.screens.components.glasskit.AvatarWithRing
import com.example.moontrade.ui.theme.Violet600
import com.example.moontrade.viewmodels.ProfileViewModel

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val nickname      by viewModel.nickname.collectAsState()
    val selectedTags  by viewModel.selectedTags.collectAsState()
    val avatarId      by viewModel.avatarId.collectAsState()
    val avatarUrl     by viewModel.avatarUrl.collectAsState()

    LaunchedEffect(Unit) {
        android.util.Log.d(
            "EditProfileScreen",
            "ON ENTER -> avatarId=$avatarId, avatarUrl=$avatarUrl"
        )
    }

    val descriptionVm by viewModel.description.collectAsState()

    val availableTags = viewModel.availableTags
    val context       = LocalContext.current

    var tempNickname by remember { mutableStateOf(nickname) }
    var tempTags     by remember { mutableStateOf(selectedTags) }
    var tempAvatarId by remember { mutableIntStateOf(avatarId) }
    var description  by remember(descriptionVm) { mutableStateOf(descriptionVm) }

    // держим локальный URI, чтобы показать превью сразу после выбора
    var pickedAvatarUri by remember { mutableStateOf<Uri?>(null) }

    // если avatarId в VM поменялся (например после uploadAvatarFromUri),
    // синхронизируем с временным состоянием экрана
    LaunchedEffect(avatarId) {
        tempAvatarId = avatarId
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            pickedAvatarUri = it

            // грузим на сервер, VM сама обновит avatarUrl и avatarId = -1
            viewModel.uploadAvatarFromUri(context, it) {
                tempAvatarId = -1
            }
        }
    }

    val scrollState = rememberScrollState()
    val cs = MaterialTheme.colorScheme

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
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // ========== PROFILE ==========
            SectionHeader(
                title = "Profile",
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AvatarWithRing(
                    size = 72.dp,
                    innerColor = Color.Transparent,
                    borderPadding = 0.dp
                ) {
                    // приоритет: локальный uri → кастомный url → встроенный аватар
                    val painter = when {
                        pickedAvatarUri != null ->
                            rememberAsyncImagePainter(pickedAvatarUri)
                        tempAvatarId == -1 && !avatarUrl.isNullOrEmpty() ->
                            rememberAsyncImagePainter(avatarUrl)
                        else ->
                            painterResource(id = avatarResIdFromLocal(tempAvatarId))
                    }

                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }

                Spacer(Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = if (tempNickname.isBlank()) "TraderX" else tempNickname,
                        style = MaterialTheme.typography.titleLarge,
                        color = cs.onSurface
                    )
                    Text(
                        text = description.takeIf { it.isNotBlank() }
                            ?: "Add short description to your profile",
                        style = MaterialTheme.typography.bodySmall,
                        color = cs.onSurface.copy(alpha = 0.6f),
                        maxLines = 2
                    )
                }
            }

            OutlinedTextField(
                value = tempNickname,
                onValueChange = { tempNickname = it },
                label = { Text("Nickname") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            // ========== TAGS ==========
            SectionHeader(
                title = "Visible tags",
                modifier = Modifier.fillMaxWidth()
            )

            if (availableTags.isEmpty()) {
                Text(
                    text = "No tags available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = cs.onSurface.copy(alpha = 0.6f)
                )
            } else {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    availableTags.forEach { tag ->
                        val selected = tag in tempTags

                        val bg = Brush.horizontalGradient(
                            if (selected) {
                                listOf(
                                    Violet600.copy(alpha = 0.9f),
                                    Violet600.copy(alpha = 1.0f),
                                    Violet600.copy(alpha = 1.1f)
                                )
                            } else {
                                listOf(
                                    Violet600.copy(alpha = 0.45f),
                                    Violet600.copy(alpha = 0.40f),
                                    Violet600.copy(alpha = 0.45f)
                                )
                            }
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(30.dp))
                                .background(bg)
                                .clickable {
                                    tempTags = if (selected) {
                                        tempTags - tag
                                    } else {
                                        tempTags + tag
                                    }
                                }
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = tag,
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            // ========== AVATAR ==========
            SectionHeader(
                title = "Avatar",
                modifier = Modifier.fillMaxWidth()
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    PrimaryGradientButton(
                        text = "Upload from gallery",
                        modifier = Modifier.weight(1f),
                        onClick = { launcher.launch(arrayOf("image/*")) }
                    )

                    if (pickedAvatarUri != null || avatarUrl != null) {
                        Image(
                            painter = rememberAsyncImagePainter(pickedAvatarUri ?: avatarUrl),
                            contentDescription = "Uploaded avatar",
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(Color.Gray.copy(alpha = 0.2f))
                        )
                    }
                }

                Text(
                    text = "Or choose one of our avatars:",
                    style = MaterialTheme.typography.bodySmall,
                    color = cs.onSurface.copy(alpha = 0.7f)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 160.dp, max = 260.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (pickedAvatarUri != null || avatarUrl != null) {
                        item(key = "custom_avatar") {
                            val selected = tempAvatarId == -1
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (selected)
                                            Violet600.copy(alpha = 0.25f)
                                        else Color.Transparent
                                    )
                                    .clickable {
                                        tempAvatarId = -1
                                    }
                                    .padding(4.dp)
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(pickedAvatarUri ?: avatarUrl),
                                    contentDescription = "Custom Avatar",
                                    modifier = Modifier.size(64.dp)
                                )
                            }
                        }
                    }

                    items((0..9).toList()) { id ->
                        val selected = id == tempAvatarId
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(
                                    if (selected)
                                        Violet600.copy(alpha = 0.25f)
                                    else Color.Transparent
                                )
                                .clickable {
                                    pickedAvatarUri = null   // переключились на встроенный аватар
                                    tempAvatarId = id
                                    viewModel.updateAvatarId(id)
                                }
                                .padding(4.dp)
                        ) {
                            Image(
                                painter = painterResource(id = avatarResIdFromLocal(id)),
                                contentDescription = "Avatar $id",
                                modifier = Modifier.size(64.dp)
                            )
                        }
                    }
                }
            }

            // ========== SAVE ==========
            Spacer(Modifier.height(4.dp))

            PrimaryGradientButton(
                text = "Save changes",
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.updateNickname(tempNickname)
                    viewModel.updateSelectedTags(tempTags)
                    viewModel.updateDescription(description)

                    // если выбрали встроенный аватар и он отличается — обновляем id
                    if (tempAvatarId != avatarId && tempAvatarId != -1) {
                        viewModel.updateAvatarId(tempAvatarId)
                    }

                    viewModel.saveProfile()


                }
            )

            Spacer(Modifier.height(12.dp))
        }
    }
}

/* helper to map built-in avatar id → drawable ------------------------------ */
@DrawableRes
private fun avatarResIdFromLocal(id: Int): Int = when (id) {
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
