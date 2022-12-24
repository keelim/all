package com.keelim.compose.demo.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DrawLine() {
    Canvas(
        modifier = Modifier.size(300.dp)
    ) {
        val height = size.height
        val width = size.width
        drawLine(
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = width, y = height),
            color = Color.Blue,
            strokeWidth = 16.0f,
            pathEffect = PathEffect.dashPathEffect(
                floatArrayOf(30f, 10f, 10f, 10f,  0f)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DrawLinePreview() {
    DrawLine()
}

@Composable
fun DrawRect() {
    Canvas(modifier = Modifier.size(300.dp)) {
        val size = Size(
            width = 200.dp.toPx(),
            height = 200.dp.toPx()
        )
        drawRoundRect(
            color = Color.Blue,
            size = size,
            topLeft = Offset(20f, 20f),
            style = Stroke(
                width = 8.dp.toPx(),
            ),
            cornerRadius = CornerRadius(
                30.dp.toPx(),
                30.dp.toPx()
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DrawRectPreview() {
    DrawRect()
}

@Composable
fun Rotate() {
    Canvas(modifier = Modifier.size(300.dp)) {
        rotate(45f) {
            drawRect(
                color = Color.Blue,
                topLeft = Offset(200f, 200f),
                size = size / 2f
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RotatePreview() {
    Rotate()
}

@Composable
fun DrawCircle() {
    Canvas(modifier = Modifier.size(300.dp)) {
        val width = size.width
        val height = size.height

        drawCircle(
            color = Color.Blue,
            center = center,
            radius = 120.dp.toPx()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DrawCirclePreview() {
    DrawCircle()
}

@Composable
fun DrawOval() {
    Canvas(modifier = Modifier.size(300.dp)) {
        val width = size.width
        val height = size.height

        drawOval(
            color = Color.Blue,
            topLeft = Offset(25.dp.toPx(), 90.dp.toPx()),
            size = Size(
                width = height - 50.dp.toPx(),
                height = height / 2 - 50.dp.toPx()
            ),
            style = Stroke(width = 12.dp.toPx())
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DrawOvalPreview() {
    DrawOval()
}

@Composable
fun GradientDemo() {
    
}

@Preview(showBackground = true)
@Composable
private fun GradientDemoPreview() {
    GradientDemo()
}


