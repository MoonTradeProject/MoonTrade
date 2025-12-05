package com.example.moontrade.ui.shaders

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas

@Composable
fun rememberShaderTime(): Float {
    val infiniteTransition = rememberInfiniteTransition(label = "shaderTimeTransition")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = androidx.compose.animation.core.tween(
                durationMillis = 20_000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shaderTimeValue"
    )
    return time
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ShaderFillBox(
    modifier: Modifier,
    shader: RuntimeShader,
    time: Float
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        shader.setFloatUniform("iResolution", width, height)
        shader.setFloatUniform("iTime", time)

        drawIntoCanvas { canvas ->
            val paint = android.graphics.Paint()
            // ВАЖНО: просто используем существующий RuntimeShader,
            // НЕ создаём новый из него
            paint.shader = shader
            canvas.nativeCanvas.drawRect(
                0f,
                0f,
                width,
                height,
                paint
            )
        }
    }
}
