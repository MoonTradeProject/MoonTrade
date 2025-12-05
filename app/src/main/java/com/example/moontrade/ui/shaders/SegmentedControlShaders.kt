package com.example.moontrade.ui.shaders

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi

// ====== METALLIC BUY/SELL ДЛЯ ПОЛЗУНКА И TRADE КНОПКИ ======

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

    color = mix(color, specColor, highlight * 0.4);

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

    color = mix(color, specColor, highlight * 0.4);

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


// ====== METALLIC ДЛЯ DROPDOWN (НЕЙТРАЛЬНЫЙ, ФИОЛЕТОВО-СИНИЙ) ======

private const val DROPDOWN_METALLIC_SHADER_SOURCE = """
uniform float2 iResolution;
uniform float  iTime;

half4 main(float2 fragCoord) {
    float2 uv = fragCoord / iResolution;

    float t = iTime * 0.1;

    float3 cTop = float3(0.05, 0.08, 0.18);
    float3 cBottom = float3(0.20, 0.10, 0.32);
    float3 base = mix(cTop, cBottom, uv.y);

    float stripePos = 0.5 + 0.35 * sin(t + 0.7);
    float dist = abs(uv.x - stripePos);
    float highlight = smoothstep(0.45, 0.0, dist);

    float3 specColor = float3(0.85, 0.80, 1.0);
    float3 color = base;

    color = mix(color, specColor, highlight * 0.40);

    float2 center = float2(0.5, 0.5);
    float2 dir = normalize(float2(0.6, -0.5));
    float diag = dot(uv - center, dir);
    float diagH = smoothstep(0.10, -0.22, diag);
    color += diagH * float3(0.12, 0.10, 0.20);

    color = clamp(color, 0.0, 1.0);

    return half4(color, 1.0);
}
"""

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun createDropdownMetallicShader(): RuntimeShader = RuntimeShader(DROPDOWN_METALLIC_SHADER_SOURCE)
