package com.example.moontrade.ui.screens.components.bars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.example.moontrade.ui.theme.Violet300
import com.example.moontrade.ui.theme.Violet600

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null
) {
    val cs = MaterialTheme.colorScheme

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Violet600.copy(alpha = 0.8f),
                                Violet300.copy(alpha = 1.0f),
                                Violet300.copy(alpha = 0.4f)
                            )
                        )
                    )
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = cs.onBackground.copy(alpha = 0.8f)
            )
        }

        if (action != null) {
            CompositionLocalProvider(
                LocalContentColor provides cs.onBackground.copy(alpha = 0.5f)
            ) {
                action()
            }
        }
    }
}
