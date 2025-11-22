package com.example.moontrade.ui.screens.components.bars

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.moontrade.R
import com.example.moontrade.ui.theme.Violet50
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
    val isDarkTheme = cs.background.luminance() < 0.5f
    val titleColor = if (isDarkTheme) Violet50 else Violet600

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
                        modifier = Modifier.padding(horizontal = 0.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_back),
                            contentDescription = "Back",
                            modifier = Modifier.size(28.dp)
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
                    overflow = TextOverflow.Ellipsis,
                    color = titleColor
                )
            }
        },
        actions = { actions?.invoke() },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = cs.background,
            scrolledContainerColor = cs.background,
            navigationIconContentColor = Color.Unspecified,
            actionIconContentColor = Color.Unspecified,
            titleContentColor = titleColor
        )
    )
}
