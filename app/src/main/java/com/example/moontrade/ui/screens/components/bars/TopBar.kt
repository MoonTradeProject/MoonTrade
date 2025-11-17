package com.example.moontrade.ui.screens.components.bars

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String? = null,
    showBack: Boolean = false,
    onBack: (() -> Unit)? = null,
    navigationContent: (@Composable () -> Unit)? = null,
    centerContent: (@Composable () -> Unit)? = null,
    actions: @Composable (() -> Unit)? = null
) {
    val cs = MaterialTheme.colorScheme

    CenterAlignedTopAppBar(
        modifier = Modifier.padding(horizontal = 10.dp),
        navigationIcon = {
            when {

                navigationContent != null -> navigationContent()

                // на остальных экранах стрелка назад
                showBack && onBack != null -> {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            }
        },
        title = {
            when {
                centerContent != null -> centerContent()
                !title.isNullOrEmpty() -> Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        actions = { actions?.invoke() },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = cs.background,
            scrolledContainerColor = cs.background,
            navigationIconContentColor = cs.primary,
            titleContentColor = cs.onBackground,
            actionIconContentColor = cs.onBackground
        )
    )
}
