package com.keelim.composeutil.component.box

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RedDot(
    modifier: Modifier = Modifier,
) {
    val value by rememberInfiniteTransition(label = "").animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 300,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "",
    )
    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = value
                scaleY = value
            }
            .size(25.dp)
            .clip(CircleShape)
            .background(Color.Red),
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewRedDot() {
    RedDot()
}

@Composable
fun GradientDot(
    modifier: Modifier = Modifier,
) {
    val value by rememberInfiniteTransition(label = "").animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing,
            ),
        ),
        label = "",
    )
    val colors = listOf(
        Color.Red,
        Color.Green,
        Color.Blue,
        Color.Black,
    )

    val gradientBrush by remember {
        mutableStateOf(
            Brush.horizontalGradient(
                colors = colors,
                startX = -10.0f,
                endX = 400.0f,
                tileMode = TileMode.Repeated,
            ),
        )
    }
    Box(
        modifier = Modifier
            .drawBehind {
                rotate(value) {
                    drawCircle(
                        gradientBrush,
                        style = Stroke(width = 12.dp.value),
                    )
                }
            }
            .size(125.dp),
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewGradientDot() {
    GradientDot()
}
