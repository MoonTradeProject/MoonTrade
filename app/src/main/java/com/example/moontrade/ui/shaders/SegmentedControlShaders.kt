//package com.example.moontrade.ui.shaders
//
//import android.graphics.RuntimeShader
//import android.os.Build
//import androidx.annotation.RequiresApi
//
//// Зелёный "живой" шейдер для Buy
//private const val BUY_SHADER_SOURCE = """
//uniform float2 iResolution;
//uniform float  iTime;
//
//half4 main(float2 fragCoord) {
//    float2 uv = fragCoord / iResolution;
//
//    float wave = sin(uv.x * 10.0 + iTime * 0.3) * 0.05
//               + sin(uv.y * 14.0 - iTime * 0.2) * 0.03;
//
//    float base = 0.7 + wave;
//    float3 color = float3(0.0, 0.65, 0.0) * base;
//
//    float2 edge = smoothstep(0.0, 0.08, uv) * (1.0 - smoothstep(0.92, 1.0, uv));
//    float glow = (edge.x + edge.y) * 0.3;
//    color += float3(0.15, 0.4, 0.15) * glow;
//
//    return half4(color, 1.0);
//}
//"""
//
//// Красный "живой" шейдер для Sell
//private const val SELL_SHADER_SOURCE = """
//uniform float2 iResolution;
//uniform float  iTime;
//
//half4 main(float2 fragCoord) {
//    float2 uv = fragCoord / iResolution;
//
//    float wave = sin(uv.x * 8.0 + iTime * 0.35) * 0.07
//               + cos(uv.y * 12.0 - iTime * 0.2) * 0.04;
//
//    float base = 0.7 + wave;
//    float3 color = float3(0.72, 0.0, 0.0) * base;
//
//    float vignette = smoothstep(1.0, 0.4, length(uv - 0.5));
//    color *= vignette;
//
//    return half4(color, 1.0);
//}
//"""
//
//@RequiresApi(Build.VERSION_CODES.TIRAMISU)
//fun createBuyShader(): RuntimeShader = RuntimeShader(BUY_SHADER_SOURCE)
//
//@RequiresApi(Build.VERSION_CODES.TIRAMISU)
//fun createSellShader(): RuntimeShader = RuntimeShader(SELL_SHADER_SOURCE)

package com.example.moontrade.ui.shaders

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi

// ====== ВОЛНОВЫЕ ШЕЙДЕРЫ ДЛЯ SEGMENTED КНОПКИ ======

private const val BUY_SHADER_SOURCE = """
uniform float2 iResolution;
uniform float  iTime;

half4 main(float2 fragCoord) {
    float2 uv = fragCoord / iResolution;

    float wave = sin(uv.x * 10.0 + iTime * 2.0) * 0.05
               + sin(uv.y * 14.0 - iTime * 1.5) * 0.03;

    float base = 0.7 + wave;
    float3 color = float3(0.0, 0.65, 0.0) * base;

    float2 edge = smoothstep(0.0, 0.08, uv) * (1.0 - smoothstep(0.92, 1.0, uv));
    float glow = (edge.x + edge.y) * 0.3;
    color += float3(0.15, 0.4, 0.15) * glow;

    return half4(color, 1.0);
}
"""

private const val SELL_SHADER_SOURCE = """
uniform float2 iResolution;
uniform float  iTime;

half4 main(float2 fragCoord) {
    float2 uv = fragCoord / iResolution;

    float wave = sin(uv.x * 8.0 + iTime * 2.5) * 0.07
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


// ====== METALLIC ШЕЙДЕРЫ ДЛЯ TRADE ACTION BUTTON ======

private const val BUY_METALLIC_SHADER_SOURCE = """
uniform float2 iResolution;
uniform float  iTime;

half4 main(float2 fragCoord) {
    float2 uv = fragCoord / iResolution;

    float t = iTime * 0.2;

    float3 cTop = float3(0.02, 0.35, 0.12);
    float3 cBottom = float3(0.0, 0.78, 0.30);
    float3 base = mix(cTop, cBottom, uv.y);

    float stripePos = 0.5 + 0.4 * sin(t);
    float dist = abs(uv.x - stripePos);
    float highlight = smoothstep(0.45, 0.0, dist);

    float3 specColor = float3(0.9, 1.0, 0.9);
    float3 color = base;

    color = mix(color, specColor, highlight * 0.45);

    float2 center = float2(0.5, 0.5);
    float2 dir = normalize(float2(0.7, 0.4));
    float diag = dot(uv - center, dir);
    float diagH = smoothstep(0.08, -0.22, diag);
    color += diagH * float3(0.10, 0.18, 0.10);

    color = clamp(color, 0.0, 1.0);

    return half4(color, 1.0);
}
"""

private const val SELL_METALLIC_SHADER_SOURCE = """
uniform float2 iResolution;
uniform float  iTime;

half4 main(float2 fragCoord) {
    float2 uv = fragCoord / iResolution;

    float t = iTime * 0.2;

    float3 cTop = float3(0.32, 0.03, 0.03);
    float3 cBottom = float3(0.82, 0.08, 0.08);
    float3 base = mix(cTop, cBottom, uv.y);

    float stripePos = 0.5 + 0.4 * sin(t + 1.2);
    float dist = abs(uv.x - stripePos);
    float highlight = smoothstep(0.45, 0.0, dist);

    float3 specColor = float3(1.0, 0.95, 0.95);
    float3 color = base;

    color = mix(color, specColor, highlight * 0.48);

    float2 center = float2(0.5, 0.5);
    float2 dir = normalize(float2(-0.7, 0.5));
    float diag = dot(uv - center, dir);
    float diagH = smoothstep(0.08, -0.22, diag);
    color += diagH * float3(0.18, 0.08, 0.08);

    color = clamp(color, 0.0, 1.0);

    return half4(color, 1.0);
}
"""

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun createBuyMetallicShader(): RuntimeShader = RuntimeShader(BUY_METALLIC_SHADER_SOURCE)

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun createSellMetallicShader(): RuntimeShader = RuntimeShader(SELL_METALLIC_SHADER_SOURCE)
