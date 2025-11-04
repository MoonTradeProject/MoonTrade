package com.example.moontrade.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Violet500, onPrimary = Color.White,
    primaryContainer = Violet800, onPrimaryContainer = Color(0xFFEDE7FF),
    secondary = Violet400, onSecondary = Color.White,
    secondaryContainer = Violet900, onSecondaryContainer = Color(0xFFE5DEFF),
    tertiary = Color(0xFF1B90FF), onTertiary = Color.White,
    tertiaryContainer = Color(0xFF08335E), onTertiaryContainer = Color(0xFFE0F1FF),

    background = DarkBackground, onBackground = TextPrimaryDark,
    surface = DarkSurface, onSurface = TextPrimaryDark,
    surfaceVariant = Ink800, onSurfaceVariant = TextSecondaryDark,

    error = RedDown, onError = Color.White,
    outline = Ink700, outlineVariant = Ink800,
    scrim = Color(0xCC000000),
    inverseSurface = Color(0xFFEAEAFF), inverseOnSurface = Color(0xFF151522),
    inversePrimary = Violet200
)

private val LightColorScheme = lightColorScheme(
    primary = Violet500, onPrimary = Color.White,
    primaryContainer = Violet200, onPrimaryContainer = Color(0xFF1A0C3E),
    secondary = Violet400, onSecondary = Color.White,
    secondaryContainer = Violet100, onSecondaryContainer = Color(0xFF22124A),
    tertiary = Color(0xFF2E7BFF), onTertiary = Color.White,
    tertiaryContainer = Color(0xFFD6E6FF), onTertiaryContainer = Color(0xFF08204A),

    background = LightBackground, onBackground = TextPrimaryLight,
    surface = LightSurface, onSurface = TextPrimaryLight,
    surfaceVariant = Color(0xFFF0E9FF), onSurfaceVariant = TextSecondaryLight,

    error = RedDown, onError = Color.White,
    outline = Color(0xFFCDD1D8), outlineVariant = Color(0xFFE6E8EE),
    scrim = Color(0x80000000),
    inverseSurface = Ink900, inverseOnSurface = Color.White,
    inversePrimary = Violet600
)

@Composable
fun MoonTradeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val extended = if (darkTheme) DarkExtendedColors else LightExtendedColors

    CompositionLocalProvider(LocalExtendedColors provides extended) {
        MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
    }
}

val MaterialTheme.extended: ExtendedColors
    @Composable get() = LocalExtendedColors.current
