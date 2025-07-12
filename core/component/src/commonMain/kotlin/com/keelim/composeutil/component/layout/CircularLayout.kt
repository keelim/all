package com.keelim.composeutil.component.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.roundToIntRect
import androidx.compose.ui.util.fastMap
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

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
            var requiredAngle = 0.0

            placeables.forEach { placeable ->
                val x = center.x + (radius * sin(Math.toRadians(requiredAngle))).toInt()
                val y = center.y + (radius * cos(Math.toRadians(requiredAngle))).toInt()

                placeable.placeRelative(x - placeable.width / 2, y - placeable.height / 2)

                requiredAngle += angularSeparation
            }
        }
    }
}

@Preview
@Composable
fun PreviewCircularLayout() {
    CircularLayout {
        (0..10).map {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(
                        color = Color(
                            red = Random.nextInt(255),
                            green = Random.nextInt(255),
                            blue = Random.nextInt(255),
                        ),
                        shape = CircleShape,
                    ),
            )
        }
    }
}
