package com.example.moontrade.ui.shaders

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi

// Зелёный "живой" шейдер для Buy
private const val BUY_SHADER_SOURCE = """
uniform float2 iResolution;
uniform float  iTime;

half4 main(float2 fragCoord) {
    float2 uv = fragCoord / iResolution;

    float wave = sin(uv.x * 10.0 + iTime * 1.5) * 0.05
               + sin(uv.y * 14.0 - iTime * 1.5) * 0.03;

    float base = 0.7 + wave;
    float3 color = float3(0.0, 0.65, 0.0) * base;

    float2 edge = smoothstep(0.0, 0.08, uv) * (1.0 - smoothstep(0.92, 1.0, uv));
    float glow = (edge.x + edge.y) * 0.3;
    color += float3(0.15, 0.4, 0.15) * glow;

    return half4(color, 1.0);
}
"""

// Красный "живой" шейдер для Sell
private const val SELL_SHADER_SOURCE = """
uniform float2 iResolution;
uniform float  iTime;

half4 main(float2 fragCoord) {
    float2 uv = fragCoord / iResolution;

    float wave = sin(uv.x * 8.0 + iTime * 1.5) * 0.07
               + cos(uv.y * 12.0 - iTime * 1.3) * 0.04;

    float base = 0.7 + wave;
    float3 color = float3(0.72, 0.0, 0.0) * base;

    float vignette = smoothstep(1.0, 0.4, length(uv - 0.5));
    color *= vignette;

    return half4(color, 1.0);
}
"""

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun createBuyShader(): RuntimeShader = RuntimeShader(BUY_SHADER_SOURCE)

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun createSellShader(): RuntimeShader = RuntimeShader(SELL_SHADER_SOURCE)
