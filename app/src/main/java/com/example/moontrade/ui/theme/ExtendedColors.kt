package com.example.moontrade.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Immutable
data class ExtendedColors(
    val success: Color,
    val danger: Color,
    val warning: Color,
    val glassSurface: Color,
    val glassCard: Color,
    val chartUp: Color,
    val chartDown: Color,
    val gradientPrimary: Brush,
    val gradientAccent: Brush,
)

val DarkExtendedColors = ExtendedColors(
    success = GreenUp,
    danger = RedDown,
    warning = AmberWarn,
    glassSurface = Color(0xFF250848),
    glassCard = Color(0xFF250848),
    chartUp = GreenUp,
    chartDown = RedDown,
    gradientPrimary = Brush.linearGradient(listOf(Violet500, Violet400)),
    gradientAccent  = Brush.linearGradient(listOf(Color(0xFF4E356A), Violet400)),
)

val LightExtendedColors = ExtendedColors(
    success = GreenUp,
    danger = RedDown,
    warning = AmberWarn,
    glassSurface = Color(0x14000000),
    glassCard = Color(0x12000000),
    chartUp = GreenUp,
    chartDown = RedDown,
    gradientPrimary = Brush.linearGradient(listOf(Violet400, Color(0x00FFFFFF))),
    gradientAccent  = Brush.linearGradient(listOf(Violet500, Violet300)),
)

val LocalExtendedColors = staticCompositionLocalOf { DarkExtendedColors }
