package com.example.moontrade.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Extended design tokens used in addition to the default Material color scheme.
 * These colors cover:
 *  - Glass surfaces
 *  - Gradients
 *  - Semantic colors (success / danger)
 *  - Chart colors
 *  - Asset % colors
 *  - Extra text colors
 *  - Avatar ring gradient
 */

@Immutable
data class ExtendedColors(

    /* Semantic colors */
    val success: Color,
    val danger: Color,
    val warning: Color,

    /* Glass UI surfaces */
    val glassSurface: Color,
    val glassCard: Color,

    /* Chart colors */
    val chartUp: Color,
    val chartDown: Color,

    /* TOTAL VALUE text color */
    val positiveText: Color,

    /* Asset percentage colors */
    val assetChangePositive: Color,
    val assetChangeNegative: Color,

    /* Brand gradients */
    val gradientPrimary: Brush,
    val gradientAccent: Brush,
    val gradientPositive: Brush,
    val gradientAvatar: Brush,
)

/* -------------------------------------------------------------------------- */
/* DARK THEME EXTENDED COLORS                                                 */
/* -------------------------------------------------------------------------- */

val DarkExtendedColors = ExtendedColors(
    success = GreenUp,
    danger = RedDown,
    warning = AmberWarn,

    glassSurface = Color(0xFF250848),
    glassCard = Violet300,

    chartUp = GreenUp,
    chartDown = RedDown,

    // TOTAL VALUE
    positiveText = GreenUpLight,

    // Asset % colors (dark)
    assetChangePositive = Color(0xFF9BFACD),
    assetChangeNegative = Color(0xFFFF9A84),

    gradientPrimary = Brush.linearGradient(
        listOf(Violet500, Violet400)
    ),
    gradientAccent = Brush.linearGradient(
        listOf(Violet500, Violet800)
    ),
    gradientPositive = Brush.linearGradient(
        listOf(GreenUp, GreenUpLight)
    ),
    gradientAvatar = Brush.linearGradient(
        listOf(Violet200, Violet500)
    )
)

/* -------------------------------------------------------------------------- */
/* LIGHT THEME EXTENDED COLORS                                                */
/* -------------------------------------------------------------------------- */

val LightExtendedColors = ExtendedColors(
    success = GreenUp,
    danger = RedDown,
    warning = AmberWarn,

    glassSurface = LightBackground,
    glassCard = Color(0xFFF1EAFF),

    chartUp = GreenUp,
    chartDown = RedDown,

    // TOTAL VALUE (calmer light green)
    positiveText = Color(0xFF3FAF7A),

    // Asset % colors (light)
    assetChangePositive = Color(0xFF26A869),
    assetChangeNegative = Color(0xFFD45B45),

    gradientPrimary = Brush.linearGradient(
        listOf(Violet100, Violet500)
    ),
    gradientAccent = Brush.linearGradient(
        listOf(Violet200, Violet100)
    ),
    gradientPositive = Brush.linearGradient(
        listOf(Color(0xFF8FF7AE), Color(0xFF48D97A))
    ),
    gradientAvatar = Brush.linearGradient(
        listOf(Violet300, Violet600)
    )
)

/* -------------------------------------------------------------------------- */

val LocalExtendedColors = staticCompositionLocalOf { DarkExtendedColors }

/**
 * Access via MaterialTheme.extended
 */
