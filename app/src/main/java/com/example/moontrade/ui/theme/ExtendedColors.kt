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

    val positiveText: Color,

    val assetChangePositive: Color,
    val assetChangeNegative: Color,

    val gradientPrimary: Brush,
    val gradientAccent: Brush,
    val gradientPositive: Brush,
    val gradientAvatar: Brush,
)

/* -------------------------------------------------------------------------- */
/* DARK THEME                                              */
/* -------------------------------------------------------------------------- */

private val MetricPositiveDark = Color(0xFFDCC9FF)
private val MetricNegativeDark = Color(0xFF9A7DE0)

val DarkExtendedColors = ExtendedColors(
    success = MetricPositiveDark,
    danger = MetricNegativeDark,
    warning = AmberWarn,

    glassSurface = Color(0xFF250848),
    glassCard = Violet300,

    chartUp = MetricPositiveDark,
    chartDown = MetricNegativeDark,

    positiveText = MetricPositiveDark,

    assetChangePositive = MetricPositiveDark,
    assetChangeNegative = MetricNegativeDark,

    gradientPrimary = Brush.linearGradient(
        listOf(Violet500, Violet400)
    ),
    gradientAccent = Brush.linearGradient(
        listOf(Violet500, Violet800)
    ),
    gradientPositive = Brush.linearGradient(
        listOf(MetricPositiveDark, MetricPositiveDark)
    ),
    gradientAvatar = Brush.linearGradient(
        listOf(Violet200, Violet500)
    )
)

/* -------------------------------------------------------------------------- */
/* LIGHT THEME*/
/* -------------------------------------------------------------------------- */

private val MetricPositiveLight = Violet600
private val MetricNegativeLight = Violet400

val LightExtendedColors = ExtendedColors(
    success = MetricPositiveLight,
    danger = MetricNegativeLight,
    warning = AmberWarn,

    glassSurface = Color(0xFFF4F5FA),
    glassCard   = Color(0xFFFFFFFF),

    chartUp = MetricPositiveLight,
    chartDown = MetricNegativeLight,

    positiveText = MetricPositiveLight,

    assetChangePositive = MetricPositiveLight,
    assetChangeNegative = MetricNegativeLight,

    gradientPrimary = Brush.linearGradient(
        listOf(
            Color(0xFF6F4BFF),
            Color(0xFF8B63FF)
        )
    ),
    gradientAccent = Brush.linearGradient(
        listOf(
            Color(0xFFFFFFFF),
            Color(0xFFF2F2FF)
        )
    ),
    gradientPositive = Brush.linearGradient(
        listOf(
            MetricPositiveLight,
            MetricPositiveLight
        )
    ),
    gradientAvatar = Brush.linearGradient(
        listOf(
            Color(0xFFFFFFFF),
            Violet300
        )
    )
)

/* -------------------------------------------------------------------------- */
/* CompositionLocal                                                           */
/* -------------------------------------------------------------------------- */

val LocalExtendedColors = staticCompositionLocalOf<ExtendedColors> {
    DarkExtendedColors
}
