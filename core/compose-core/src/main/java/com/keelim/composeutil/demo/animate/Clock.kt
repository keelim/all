package com.keelim.composeutil.demo.animate

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ClockAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val clockAnimation by
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 720f,
            animationSpec = infiniteRepeatable(animation = tween(), repeatMode = RepeatMode.Restart),
            label = "",
        )

    var strokeWidth by remember { mutableFloatStateOf(0f) }
    val currentHour by remember(clockAnimation) {
        derivedStateOf { clockAnimation.toInt() / 30 }
    }
    val hours: List<Int> = remember { List(12) { it } }
    val dotsVisibility = remember(currentHour) {
        hours.map { index ->
            when {
                index > currentHour -> false
                index > currentHour - 12 -> true
                else -> false
            }
        }
    }
    Spacer(
        modifier =
        Modifier
            .fillMaxSize()
            .onGloballyPositioned { strokeWidth = (it.size.width / 24).toFloat() }
            .drawBehind {
                val center = Offset(size.width / 2, size.height / 2)
                val endOffset = Offset(
                    size.width / 2,
                    size.height / 2 - calculateClockHandLength(size.height / 2, currentHour),
                )
                rotate(clockAnimation, pivot = center) {
                    drawLine(
                        color = Color.White,
                        start = center,
                        end = endOffset,
                        strokeWidth = strokeWidth,
                    )
                }
                hours.forEach {
                    if (!dotsVisibility[it]) return@forEach
                    val degree = it * 30f
                    rotate(degree) {
                        val start = Offset(size.width / 2, 0f)
                        val end = Offset(size.width / 2, strokeWidth)
                        drawLine(
                            color = Color.White,
                            start = start,
                            end = end,
                            strokeWidth = strokeWidth,
                        )
                    }
                }
            },
    )
}

@Preview
@Composable
private fun ClockPreview() {
    ClockAnimation()
}

private fun calculateClockHandLength(maxHeight: Float, currentHour: Int): Float {
    val stepHeight = maxHeight / 12
    // Height decreases first 360 deg, then increases again
    return stepHeight * if (currentHour < 12) {
        12 - 1 - currentHour
    } else {
        currentHour - 12
    }
}
