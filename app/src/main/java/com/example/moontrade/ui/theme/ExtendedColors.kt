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
    val gradientPositive: Brush,
    val gradientAvatar: Brush      // фиолетовое кольцо аватара и заголовки
)

/* ---------------- DARK THEME ---------------- */

val DarkExtendedColors = ExtendedColors(
    success = Color(0xFF78F29A),
    danger = Color(0xFFFF5A6B),
    warning = AmberWarn,

    // фон стеклянных карточек
    glassSurface = Color(0xFF2F1B52),
    glassCard = Color(0xFF261541),

    chartUp = Color(0xFF78F29A),
    chartDown = RedDown,

    // общий градиент фона
    gradientPrimary = Brush.linearGradient(
        listOf(Color(0xFF5E45C7), Color(0xFF2A1746))
    ),

    // градиент больших карточек (чуть светлее сверху)
    gradientAccent = Brush.linearGradient(
        listOf(Color(0xFF6A50D8), Color(0xFF2D194C))
    ),

    // зелёная пилюля ACTIVE
    gradientPositive = Brush.linearGradient(
        listOf(Color(0xFF8FF7AE), Color(0xFF48D97A))
    ),

    // фиолетовое кольцо аватара
    gradientAvatar = Brush.linearGradient(
        listOf(Color(0xFFC6B8FF), Color(0xFF7A5CFF))
    )
)

/* ---------------- LIGHT THEME ---------------- */

val LightExtendedColors = ExtendedColors(
    success = Color(0xFF34C759),
    danger = RedDown,
    warning = AmberWarn,

    glassSurface = Color(0x22AFA2FF),
    glassCard = Color(0x11AFA2FF),

    chartUp = Color(0xFF34C759),
    chartDown = RedDown,

    gradientPrimary = Brush.linearGradient(
        listOf(Color(0xFF8E79FF), Color(0x33FFFFFF))
    ),

    gradientAccent = Brush.linearGradient(
        listOf(Color(0xFFF7F5FD), Color(0xFFB3A9E3))
    ),

    gradientPositive = Brush.linearGradient(
        listOf(Color(0xFF8FF7AE), Color(0xFF48D97A))
    ),

    gradientAvatar = Brush.linearGradient(
        listOf(Color(0xFFB9A8FF), Color(0xFF7A5CFF))
    )
)

/* ---------------- PROVIDER ---------------- */

val LocalExtendedColors = staticCompositionLocalOf { DarkExtendedColors }
