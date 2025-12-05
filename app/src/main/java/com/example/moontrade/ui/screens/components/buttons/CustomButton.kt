package com.example.moontrade.ui.screens.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Universal selfish‑styled button used across onboarding & auth flows.
 *
 * @param text            Caption shown inside the button. Ignored if [fillImage] = true.
 * @param onClick         Lambda triggered on tap.
 * @param backgroundColor Defaults to `MaterialTheme.colorScheme.primary`.
 * @param textColor       Defaults to `MaterialTheme.colorScheme.onPrimary`.
 * @param icon            Optional leading icon.
 * @param fillImage       If `true` – draw only [icon] centered (used for Google‑sign‑in white btn).
 * @param rounded         Corner radius. By default 12dp.
 * @param paddingNeeded   If `false`, removes default horizontal padding for snug layouts.
 * @param buttonWidth/Height  Fixed size when non‑null, else `wrapContent`.
 */
@Composable
fun CustomButton(
    text: String = "",
    onClick: () -> Unit,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    icon: Painter? = null,
    fillImage: Boolean = false,
    rounded: Dp = 12.dp,
    paddingNeeded: Boolean = true,
    buttonWidth: Dp? = null,
    buttonHeight: Dp? = null,
) {
    val shape = RoundedCornerShape(rounded)
    Button(
        onClick = onClick,
        shape = shape,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        border = if (backgroundColor == Color.Transparent) BorderStroke(1.dp, MaterialTheme.colorScheme.outline) else null,
        contentPadding = if (paddingNeeded) PaddingValues(horizontal = 24.dp, vertical = 12.dp) else PaddingValues(0.dp),
        modifier = Modifier.then(
            if (buttonWidth != null && buttonHeight != null) Modifier.size(buttonWidth, buttonHeight)
            else if (buttonWidth != null) Modifier.width(buttonWidth)
            else if (buttonHeight != null) Modifier.height(buttonHeight)
            else Modifier.wrapContentSize()
        )
    ) {
        if (fillImage) {
            icon?.let {
                Image(painter = it, contentDescription = null, modifier = Modifier.size(24.dp))
            }
        } else {
            if (icon != null) {
                Image(painter = icon, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(text = text, color = textColor, style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp))
        }
    }
}
