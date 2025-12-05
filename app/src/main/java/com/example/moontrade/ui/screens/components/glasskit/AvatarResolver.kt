package com.example.moontrade.ui.screens.components.glasskit

import androidx.annotation.DrawableRes
import com.example.moontrade.R
@DrawableRes
fun resolveAvatarRes(id: Int): Int = when (id) {
    0 -> R.drawable.avatar_0
    1 -> R.drawable.avatar_1
    2 -> R.drawable.avatar_2
    3 -> R.drawable.avatar_3
    4 -> R.drawable.avatar_4
    5 -> R.drawable.avatar_5
    6 -> R.drawable.avatar_6
    7 -> R.drawable.avatar_7
    8 -> R.drawable.avatar_8
    else -> R.drawable.img
}