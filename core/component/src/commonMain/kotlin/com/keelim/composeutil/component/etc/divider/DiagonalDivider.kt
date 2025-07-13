package com.keelim.composeutil.component.etc.divider

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DividerDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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

@Preview(showBackground = true)
@Composable
fun PreviewDiagnoalDivider() {
    DiagnoalDivider(
        width = 100.dp,
        ratio = 0.2f,
        modifier = Modifier.height(32.dp),
        thickness = 2.dp,
        color = Color.Red,
    )
}
