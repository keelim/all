package com.keelim.composeutil.util

import androidx.compose.ui.graphics.Color
import kotlin.random.Random
import kotlin.random.nextInt

fun randomColor(): Color = Color(
    red = Random.nextInt(0..255),
    green = Random.nextInt(0..255),
    blue = Random.nextInt(0..255),
)
