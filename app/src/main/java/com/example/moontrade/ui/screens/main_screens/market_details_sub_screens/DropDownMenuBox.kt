package com.example.moontrade.ui.screens.main_screens.market_details_sub_screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moontrade.ui.shaders.ShaderFillBox
import com.example.moontrade.ui.shaders.createDropdownMetallicShader
import com.example.moontrade.ui.shaders.rememberShaderTime

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun DropdownMenuBox(
    selected: String,
    options: List<String>,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var boxWidthPx by remember { mutableStateOf(0) }

    val density = LocalDensity.current
    val boxShape = RoundedCornerShape(14.dp)

    val interactionSource = remember { MutableInteractionSource() }

    val shader = remember { createDropdownMetallicShader() }
    val shaderTime = rememberShaderTime()

    val arrowRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 180),
        label = "dropdownArrowRotation"
    )

    val menuScale by animateFloatAsState(
        targetValue = if (expanded) 1f else 0.92f,
        animationSpec = tween(durationMillis = 160),
        label = "dropdownMenuScale"
    )

    val menuAlpha by animateFloatAsState(
        targetValue = if (expanded) 1f else 0f,
        animationSpec = tween(durationMillis = 160),
        label = "dropdownMenuAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                boxWidthPx = coordinates.size.width
            }
            .clip(boxShape)
    ) {
        // Фон-металлик (размер берёт от Box)
        ShaderFillBox(
            modifier = Modifier.matchParentSize(),
            shader = shader,
            time = shaderTime
        )

        // Контент задаёт высоту (wrapContent по тексту + паддинги)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.30f), boxShape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    expanded = true
                }
                .padding(horizontal = 12.dp, vertical = 10.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = selected,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 32.dp),
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFE7E7F5)
            )

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "dropdown arrow",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(22.dp)
                    .rotate(arrowRotation),
                tint = Color(0xFFC8C8FF)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(density) { boxWidthPx.toDp() })
                .background(Color(0xFF05060B), RoundedCornerShape(14.dp))
        ) {
            Column(
                modifier = Modifier.graphicsLayer {
                    scaleX = menuScale
                    scaleY = menuScale
                    alpha = menuAlpha
                }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option,
                                textAlign = TextAlign.Center,
                                fontSize = 14.sp,
                                color = if (option == selected) {
                                    Color(0xFFC29BFF)
                                } else {
                                    Color(0xFFF5F5FF)
                                },
                                fontWeight = if (option == selected) {
                                    FontWeight.SemiBold
                                } else {
                                    FontWeight.Normal
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        onClick = {
                            expanded = false
                            onSelected(option)
                        }
                    )
                }
            }
        }
    }
}
