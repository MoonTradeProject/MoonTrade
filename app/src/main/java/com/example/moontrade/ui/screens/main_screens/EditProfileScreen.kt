@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.example.moontrade.ui.screens.main_screens

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.moontrade.ui.screens.components.bars.SectionHeader
import com.example.moontrade.ui.screens.components.bars.TopBar
import com.example.moontrade.ui.screens.components.buttons.PrimaryGradientButton
import com.example.moontrade.viewmodels.ProfileViewModel
import com.example.moontrade.ui.theme.Violet600
import com.example.moontrade.ui.screens.components.glasskit.GlassCard
import com.example.moontrade.ui.screens.components.glasskit.resolveAvatarRes

private const val NICKNAME_LIMIT = 15
private const val BIO_LIMIT = 155

@SuppressLint("MutableCollectionMutableState")
@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val nickname      by viewModel.nickname.collectAsState()
    val selectedTags  by viewModel.selectedTags.collectAsState()
    val avatarId      by viewModel.avatarId.collectAsState()
    val avatarUrl     by viewModel.avatarUrl.collectAsState()
    val descriptionVm by viewModel.description.collectAsState()

    val availableTags = viewModel.availableTags
    val clipboard     = LocalClipboardManager.current
    val context       = LocalContext.current

    var tempNickname by remember { mutableStateOf(nickname) }
    var tempTag      by remember(selectedTags) { mutableStateOf(selectedTags.firstOrNull()) }
    var tempAvatarId by remember { mutableIntStateOf(avatarId) }

    var aboutState by remember(descriptionVm) {
        mutableStateOf(
            TextFieldValue(
                text = descriptionVm,
                selection = TextRange(descriptionVm.length)
            )
        )
    }
    val aboutText = aboutState.text

    var pickedAvatarUri by remember { mutableStateOf<Uri?>(null) }

    var isEditingAbout by remember { mutableStateOf(false) }
    var showAboutSheet by remember { mutableStateOf(false) }

    val aboutFocusRequester = remember { FocusRequester() }
    var hasChanges by remember { mutableStateOf(false) }

    LaunchedEffect(isEditingAbout) {
        if (isEditingAbout) {
            aboutState = aboutState.copy(selection = TextRange(aboutState.text.length))
            aboutFocusRequester.requestFocus()
        }
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            pickedAvatarUri = it
            hasChanges = true
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
        },
        floatingActionButton = {
            if (hasChanges) {
                Surface(
                    shape = RoundedCornerShape(26.dp),
                    shadowElevation = 12.dp,
                    color = Color.Transparent
                ) {
                    PrimaryGradientButton(
                        text = "Save changes",
                        onClick = {
                            viewModel.updateNickname(tempNickname)
                            viewModel.updateDescription(aboutState.text)
                            viewModel.updateSelectedTags(
                                tempTag?.let { listOf(it) } ?: emptyList()
                            )
                            if (tempAvatarId != avatarId && tempAvatarId != -1) {
                                viewModel.updateAvatarId(tempAvatarId)
                            }
                            viewModel.saveProfile()
                            isEditingAbout = false
                            hasChanges     = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->

        Box(
            Modifier
                .fillMaxSize()
                .padding(bottom = if (hasChanges) 88.dp else 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // ---------- PROFILE ----------
                SectionHeader("Profile")

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EditProfileAvatar(
                        avatarId = tempAvatarId,
                        avatarUrl = avatarUrl,
                        pickedAvatarUri = pickedAvatarUri
                    )
                    Spacer(Modifier.width(20.dp))

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = tempNickname.ifBlank { "TraderX" },
                            style = MaterialTheme.typography.titleLarge,
                            color = cs.onSurface
                        )
                        Text(
                            text = aboutText.ifBlank { "Add short description to your profile" },
                            style = MaterialTheme.typography.bodySmall,
                            color = cs.onSurface.copy(alpha = 0.6f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                // ---------- NICKNAME ----------
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Nickname",
                        style = MaterialTheme.typography.labelMedium,
                        color = cs.onSurface.copy(alpha = 0.8f)
                    )
                    TextField(
                        value = tempNickname,
                        onValueChange = { new ->
                            if (new.length <= NICKNAME_LIMIT) {
                                tempNickname = new
                                hasChanges = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 60.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        placeholder = { Text("Enter your nickname") },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = cs.surfaceVariant.copy(alpha = 0.98f),
                            unfocusedContainerColor = cs.surfaceVariant.copy(alpha = 0.9f),
                            disabledContainerColor = cs.surfaceVariant,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            cursorColor = cs.primary
                        )
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = "${tempNickname.length} / $NICKNAME_LIMIT",
                            style = MaterialTheme.typography.labelSmall,
                            color = cs.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
                // ---------- ABOUT ME ----------
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {

                    Text(
                        text = "About me",
                        style = MaterialTheme.typography.labelMedium,
                        color = cs.onSurface.copy(alpha = 0.8f)
                    )
                    if (!isEditingAbout) {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) { showAboutSheet = true }
                        ) {
                            GlassCard(
                                modifier = Modifier.fillMaxWidth(),
                                corner = 24.dp
                            ) {
                                Text(
                                    text = aboutText.ifBlank { "Add a few lines about yourself" },
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = cs.onSurface.copy(
                                        alpha = if (aboutText.isBlank()) 0.6f else 0.9f
                                    ),
                                    lineHeight = 20.sp,
                                    modifier = Modifier.padding(
                                        horizontal = 14.dp,
                                        vertical = 8.dp
                                    )
                                )
                            }
                        }
                    } else {
                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            corner = 24.dp
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                BasicTextField(
                                    value = aboutState,
                                    onValueChange = { newValue ->
                                        if (newValue.text.length <= BIO_LIMIT) {
                                            aboutState = newValue
                                            hasChanges = true
                                        } else {
                                            aboutState = newValue.copy(
                                                text = newValue.text.take(BIO_LIMIT),
                                                selection = TextRange(BIO_LIMIT)
                                            )
                                            hasChanges = true
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .focusRequester(aboutFocusRequester),
                                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                                        color = cs.onSurface
                                    ),
                                    maxLines = 8,
                                    cursorBrush = SolidColor(cs.primary),
                                    decorationBox = { innerTextField ->
                                        if (aboutState.text.isEmpty()) {
                                            Text(
                                                text = "Add a few lines about yourself",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = cs.onSurface.copy(alpha = 0.6f)
                                            )
                                        }
                                        innerTextField()
                                    }
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = "${aboutText.length} / $BIO_LIMIT",
                            style = MaterialTheme.typography.labelSmall,
                            color = cs.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
                // ---------- TAGS ----------
                SectionHeader("Visible tags")

                if (availableTags.isEmpty()) {
                    Text(
                        text = "No tags available",
                        style = MaterialTheme.typography.bodyMedium,
                        color = cs.onSurface.copy(alpha = 0.6f)
                    )
                } else {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        availableTags.forEach { tag ->
                            val selected = tempTag == tag

                            val bg = Brush.horizontalGradient(
                                if (selected) {
                                    listOf(
                                        Violet600.copy(alpha = 0.98f),
                                        Violet600
                                    )
                                } else {
                                    listOf(
                                        Violet600.copy(alpha = 0.5f),
                                        Violet600.copy(alpha = 0.55f)
                                    )
                                }
                            )

                            Box(
                                modifier = Modifier
                                    .heightIn(min = 44.dp)
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(bg)
                                    .clickable {
                                        tempTag = if (selected) null else tag
                                        hasChanges = true
                                    }
                                    .padding(horizontal = 18.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = tag,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
                // ---------- AVATAR ----------
                SectionHeader("Avatar")

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    PrimaryGradientButton(
                        text = "Upload from gallery",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        onClick = { launcher.launch(arrayOf("image/*")) }
                    )
                    Text(
                        text = "Or choose one of our avatars:",
                        style = MaterialTheme.typography.bodySmall,
                        color = cs.onSurface.copy(alpha = 0.7f)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 190.dp, max = 290.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (pickedAvatarUri != null || avatarUrl != null) {
                            item(key = "custom_avatar") {
                                val selected = tempAvatarId == -1
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(18.dp))
                                        .background(
                                            if (selected)
                                                Violet600.copy(alpha = 0.28f)
                                            else Color.Transparent
                                        )
                                        .clickable {
                                            tempAvatarId = -1
                                            hasChanges = true
                                        }
                                        .padding(4.dp)
                                ) {
                                    AsyncImage(
                                        model = pickedAvatarUri ?: avatarUrl,
                                        contentDescription = "Custom Avatar",
                                        modifier = Modifier
                                            .size(80.dp)
                                            .clip(RoundedCornerShape(18.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }

                        items((0..9).toList()) { id ->
                            val selected = id == tempAvatarId
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(
                                        if (selected)
                                            Violet600.copy(alpha = 0.28f)
                                        else Color.Transparent
                                    )
                                    .clickable {
                                        pickedAvatarUri = null
                                        tempAvatarId = id
                                        hasChanges = true
                                        viewModel.updateAvatarId(id)
                                    }
                                    .padding(4.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = resolveAvatarRes(id)),
                                    contentDescription = "Avatar $id",
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(RoundedCornerShape(18.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }
            // ---------- BOTTOM SHEET ----------
            if (showAboutSheet) {
                AboutEditSheet(
                    hasText = aboutText.isNotBlank(),
                    onDismiss = { showAboutSheet = false },
                    onEdit = {
                        showAboutSheet = false
                        isEditingAbout = true
                    },
                    onCopy = {
                        clipboard.setText(AnnotatedString(aboutText))
                        showAboutSheet = false
                    }
                )
            }
        }
    }
}


@Composable
private fun EditProfileAvatar(
    avatarId: Int,
    avatarUrl: String?,
    pickedAvatarUri: Uri?,
    modifier: Modifier = Modifier,
    outerSize: Dp = 120.dp,
    innerSize: Dp = 80.dp
) {
    val cs = MaterialTheme.colorScheme

    Box(
        modifier = modifier
            .size(outerSize)
            .background(
                Brush.radialGradient(
                    listOf(
                        cs.primary.copy(alpha = 1.0f),
                        Color.Transparent
                    )
                ),
                CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(innerSize)
                .clip(CircleShape)
                .border(2.dp, cs.primary, CircleShape)
                .background(cs.surfaceVariant.copy(alpha = 0.6f))
        ) {
            when {
                pickedAvatarUri != null -> {
                    AsyncImage(
                        model = pickedAvatarUri,
                        contentDescription = "avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                avatarId == -1 && !avatarUrl.isNullOrEmpty() -> {
                    AsyncImage(
                        model = avatarUrl,
                        contentDescription = "avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                else -> {
                    Image(
                        painter = painterResource(resolveAvatarRes(avatarId)),
                        contentDescription = "avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun AboutEditSheet(
    hasText: Boolean,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onCopy: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.55f))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onDismiss() }
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 12.dp, vertical = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 10.dp)
                    .width(44.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(50))
                    .background(cs.onSurface.copy(alpha = 0.4f))
            )

            Surface(
                shape = RoundedCornerShape(22.dp),
                color = cs.surface.copy(alpha = 0.98f),
                tonalElevation = 8.dp
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clickable { onEdit() }
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = null,
                            tint = cs.primary
                        )
                        Spacer(Modifier.width(14.dp))
                        Text(
                            text = "Edit “About me”",
                            style = MaterialTheme.typography.bodyLarge,
                            color = cs.onSurface
                        )
                    }

                    Divider(color = cs.outline.copy(alpha = 0.15f))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clickable(enabled = hasText) { if (hasText) onCopy() }
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ContentCopy,
                            contentDescription = null,
                            tint = if (hasText) cs.primary else cs.onSurface.copy(alpha = 0.4f)
                        )
                        Spacer(Modifier.width(14.dp))
                        Text(
                            text = "Copy text",
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (hasText) cs.onSurface else cs.onSurface.copy(alpha = 0.4f)
                        )
                    }
                }
            }
        }
    }
}

