package com.keelim.composeutil.component.etc.divider

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DividerDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
fun DiagnoalDivider(
    width: Dp,
    ratio: Float,
    modifier: Modifier = Modifier,
    thickness: Dp = DividerDefaults.Thickness,
    color: Color = DividerDefaults.color,
) = Canvas(modifier.fillMaxHeight().width(width)) {
    val startRatio = thickness.toPx() / 2 + width.toPx() * ratio
    val endRatio = thickness.toPx() / 2 + width.toPx() * (1 - ratio)
    drawLine(
        color = color,
        strokeWidth = thickness.toPx(),
        start = Offset(startRatio, size.height),
        end = Offset(endRatio, 0f),
    )
}
