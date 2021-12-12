package com.keelim.compose.utils

import androidx.compose.ui.graphics.Color

fun String.toColor(): Color =
    Color(android.graphics.Color.parseColor(this))