package com.keelim.compose.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun dp2Sp(dp: Dp) = with(LocalDensity.current) { dp.toSp() }