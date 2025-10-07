package com.keelim.composeutil.component.layout

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.roundToIntRect
import androidx.compose.ui.util.fastMap

@Composable
fun CircularLayout(
    modifier: Modifier = Modifier,
    radius: Float = 250f,
    content: @Composable () -> Unit,
) {
    Layout(modifier = modifier, content = content) { measurables, constraints ->
        val placeables = measurables.fastMap { it.measure(constraints) }
        val angularSeparation = 360 / placeables.size

        val boundedRectangle = Rect(
            center = Offset(
                x = 0f,
                y = 0f,
            ),
            radius = radius + placeables.first().height,
        ).roundToIntRect()
        val center = IntOffset(boundedRectangle.width / 2, boundedRectangle.height / 2)

        layout(boundedRectangle.width, boundedRectangle.height) {
            val requiredAngle = 0.0

            placeables.forEach { placeable ->
                // val x = center.x + (radius * sin(Math.toRadians(requiredAngle))).toInt()
                // val y = center.y + (radius * cos(Math.toRadians(requiredAngle))).toInt()
//
                // placeable.placeRelative(x - placeable.width / 2, y - placeable.height / 2)
//
                // requiredAngle += angularSeparation
            }
        }
    }
}


