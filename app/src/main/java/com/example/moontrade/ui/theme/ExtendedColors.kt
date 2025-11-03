package com.example.moontrade.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class MoonTradeExtendedColors(
    val topBarBg: Color,
    val topBarText: Color,
    val dropdownBg: Color,
    val avatarCard: Color,
    val chip: Color,
    val chipText: Color,
    val nickname: Color,
    val portfolioCard: Color,
    val portfolioLabel: Color,
    val portfolioRoi: Color,
    val gold: Color,
)

val LightExtendedColors = MoonTradeExtendedColors(
    topBarBg = LightTopBar,
    topBarText = LightText,
    dropdownBg = LightDropdown,
    avatarCard = LightAvatarCard,
    chip = LightChip,
    chipText = LightText,
    nickname = LightText,
    portfolioCard = LightPortfolioCard,
    portfolioLabel = LightPortfolioLabel,
    portfolioRoi = LightPortfolioRoi,
    gold = Gold
)

val DarkExtendedColors = MoonTradeExtendedColors(
    topBarBg = DarkTopBar,
    topBarText = DarkText,
    dropdownBg = DarkDropdown,
    avatarCard = DarkAvatarCard,
    chip = DarkChip,
    chipText = DarkText,
    nickname = DarkText,
    portfolioCard = DarkPortfolioCard,
    portfolioLabel = DarkPortfolioLabel,
    portfolioRoi = DarkPortfolioRoi,
    gold = Gold
)

internal val LocalExtendedColors = staticCompositionLocalOf { LightExtendedColors }

val androidx.compose.material3.MaterialTheme.extended: MoonTradeExtendedColors
    @Composable get() = LocalExtendedColors.current
