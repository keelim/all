package com.keelim.composeutil.component.card

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun GradientAnimationBorderCard(
    colors: List<Color>,
    duration: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val infiniteColorTransition = rememberInfiniteTransition(label = "")
    val degrees by infiniteColorTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = duration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "",
    )
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = CircleShape,
    ) {
        Surface(
            modifier = Modifier
                .padding(2.dp) // Border width
                .drawWithContent {
                    rotate(degrees) {
                        drawCircle(
                            brush = Brush.linearGradient(colors),
                            radius = size.width,
                            blendMode = BlendMode.SrcIn,
                        )
                    }
                    drawContent()
                },
            shape = CircleShape,
            content = content,
        )
    }
}

@Preview
@Composable
fun PreviewGradientAnimationBorderCard() {
    Column {
        GradientAnimationBorderCard(
            colors = listOf(Color.Red, Color.Blue),
            duration = 5000,
            onClick = {},
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Hello, World!",
            )
        }
    }
}
