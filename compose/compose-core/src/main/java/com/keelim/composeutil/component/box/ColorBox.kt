package com.keelim.composeutil.component.box

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import kotlin.random.Random

@Composable
fun ColorBox() {
    Box(
        modifier =
        Modifier
            .border(width = 2.dp, color = Color.Black)
            .background(randomColor())
            .width(40 * randomInt().dp)
            .height(10 * randomInt().dp),
    )
}

private fun randomInt() = Random.nextInt(1, 4)

private fun randomColor() =
    when (randomInt()) {
        1 -> Color.Red
        2 -> Color.Green
        else -> Color.Blue
    }

private fun simpleFlexBoxMeasurePolicy(): MeasurePolicy =
    MeasurePolicy { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }
        layout(
            constraints.maxWidth,
            constraints.maxHeight,
        ) {
            var yPos = 0
            var xPos = 0
            var maxY = 0
            placeables.forEach { placeable ->
                if (xPos + placeable.width > constraints.maxWidth) {
                    xPos = 0
                    yPos += maxY
                    maxY = 0
                }
                placeable.placeRelative(
                    x = xPos,
                    y = yPos,
                )
                xPos += placeable.width
                if (maxY < placeable.height) {
                    maxY = placeable.height
                }
            }
        }
    }

@Preview(showBackground = true)
@Composable
private fun PreviewColorBox() {
    ColorBox()
}

@Composable
fun SimpleFlexBox(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Layout(
        modifier = modifier,
        content = content,
        measurePolicy = simpleFlexBoxMeasurePolicy(),
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewSimpleFlexBox() {
    SimpleFlexBox {
        for (i in 0..1000) {
            ColorBox()
        }
    }
}
