package com.keelim.composeutil.demo.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space8

@Composable
fun DrawLine(
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier,
    ) {
        val height = size.height
        val width = size.width
        drawLine(
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = width, y = height),
            color = Color.Blue,
            strokeWidth = 16.0f,
            pathEffect = PathEffect.dashPathEffect(
                floatArrayOf(30f, 10f, 10f, 10f, 0f),
            ),
        )
    }
}

@Composable
fun DrawRect(
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val size = Size(
            width = 200.dp.toPx(),
            height = 200.dp.toPx(),
        )
        drawRoundRect(
            color = Color.Blue,
            size = size,
            topLeft = Offset(20f, 20f),
            style = Stroke(
                width = space8.toPx(),
            ),
            cornerRadius = CornerRadius(
                30.dp.toPx(),
                30.dp.toPx(),
            ),
        )
    }
}

@Composable
fun Rotate(
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        rotate(45f) {
            drawRect(
                color = Color.Blue,
                topLeft = Offset(200f, 200f),
                size = size / 2f,
            )
        }
    }
}

@Composable
fun DrawCircle(
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        drawCircle(
            color = Color.Blue,
            center = center,
            radius = 120.dp.toPx(),
        )
    }
}

@Composable
fun DrawOval(
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        drawOval(
            color = Color.Blue,
            topLeft = Offset(25.dp.toPx(), 90.dp.toPx()),
            size = Size(
                width = height - 50.dp.toPx(),
                height = height / 2 - 50.dp.toPx(),
            ),
            style = Stroke(width = space12.toPx()),
        )
    }
}

@Composable
fun DrawText(
    text: String,
    modifier: Modifier = Modifier,
) {
    val paint = remember {
        Paint().asFrameworkPaint().apply {
            // paint configuration
            this.textSize = 60f
        }
    }
    Canvas(
        modifier = modifier
    ) {
        drawIntoCanvas {
            it.nativeCanvas.drawText(
                text,
                20f,
                200f,
                paint
            )
        }
    }
}

@Composable
fun DrawTriangle(
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val path = Path().apply {
            moveTo(size.width / 2, 0f)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        drawPath(path = path, color = Color.Magenta)
    }
}

@Composable
fun DrawArc(
    color: Color,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier
    ) {
        drawArc(
            color = color,
            startAngle = 0f,
            sweepAngle = 270f,
            useCenter = true,
            topLeft = Offset.Zero,
            size = size
        )
    }
}

@Composable
fun DrawShader(
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val shader = Brush.linearGradient(
            colors = listOf(Color.Red, Color.Blue),
            start = Offset.Zero,
            end = Offset(size.width, size.height)
        )
        drawRect(brush = shader)
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDrawCanvas() {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        val modifier = Modifier.size(10.dp)
        item {
            DrawLine(
                modifier = modifier,
            )
        }
        item {
            DrawRect(
                modifier = modifier,
            )
        }
        item {
            DrawText(
                text = "Hello, World!",
                modifier = modifier,
            )
        }
        item {
            Rotate(
                modifier = modifier,
            )
        }
        item {
            DrawCircle(
                modifier = modifier,
            )
        }
        item {
            DrawOval(
                modifier = modifier,
            )
        }
        item {
            DrawTriangle(
                modifier = modifier,
            )
        }
        item {
            DrawArc(
                color = Color.Blue,
                modifier = modifier,
            )
        }
    }
}
