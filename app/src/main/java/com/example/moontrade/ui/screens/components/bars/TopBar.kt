package com.example.moontrade.ui.screens.components.bars

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.moontrade.ui.theme.Violet600

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
        modifier = Modifier
            .padding(top = 10.dp, start = 10.dp, end = 10.dp),
        windowInsets = WindowInsets(0),
        navigationIcon = {
            when {
                navigationContent != null -> navigationContent()

                showBack && onBack != null -> {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
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
            navigationIconContentColor = Violet600,
            titleContentColor = Violet600,
            actionIconContentColor = Violet600
        )
    )
}
