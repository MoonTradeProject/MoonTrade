package com.example.moontrade.ui.screens.components.bars

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String? = null,
    showBack: Boolean = false,
    onBack: (() -> Unit)? = null,
    navigationContent: (@Composable () -> Unit)? = null,   // ⬅️ новый левый слот
    centerContent: (@Composable () -> Unit)? = null,
    actions: @Composable (() -> Unit)? = null
) {
    CenterAlignedTopAppBar( // чтобы заголовок реально был по центру
        navigationIcon = {
            when {
                navigationContent != null -> navigationContent()
                showBack && onBack != null -> {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            }
        },
        title = {
            when {
                centerContent != null -> centerContent()
                !title.isNullOrEmpty() -> Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        },
        actions = { actions?.invoke() }
        // при желании добавь цвета:
        // colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        //     containerColor = MaterialTheme.extended.glassCard
        // )
    )
}
