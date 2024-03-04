package com.keelim.composeutil.util

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FloatAnimationSpec
import androidx.compose.animation.core.FloatTweenSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds

fun Modifier.onTouchHeld(
    pollDelay: Duration,
    onTouchHeld: (timeElapsed: Duration) -> Unit,
) = this.composed {
    val scope = rememberCoroutineScope()
    pointerInput(onTouchHeld) {
        awaitEachGesture {
            val initialDown = awaitFirstDown(requireUnconsumed = false)
            val initialDownTime = System.nanoTime()
            val initialTouchHeldJob =
                scope.launch {
                    while (initialDown.pressed) {
                        val timeElapsed = System.nanoTime() - initialDownTime
                        onTouchHeld.invoke(timeElapsed.nanoseconds)
                        delay(pollDelay)
                    }
                }
            waitForUpOrCancellation()
            initialTouchHeldJob.cancel()
        }
    }
}

fun Modifier.onTouchHeldAnimated(
    easing: Easing = FastOutSlowInEasing,
    pollDelay: Duration = 500.milliseconds,
    targetPollDelay: Duration = pollDelay,
    animationDuration: Duration = 5.seconds,
    onTouchHeld: () -> Unit,
) = this.composed {
    val scope = rememberCoroutineScope()
    pointerInput(onTouchHeld) {
        val animationSpec: FloatAnimationSpec = FloatTweenSpec(
            animationDuration.inWholeSeconds.toInt(),
            0,
            easing,
        )
        awaitEachGesture {
            val initialDown = awaitFirstDown(requireUnconsumed = false)
            val initialTouchHeldJob = scope.launch {
                var currentPlayTime = 0.milliseconds
                var delay = pollDelay
                while (initialDown.pressed) {
                    onTouchHeld()
                    delay(delay.inWholeMilliseconds)
                    currentPlayTime += delay
                    delay = animationSpec.getValueFromNanos(
                        currentPlayTime.inWholeNanoseconds,
                        pollDelay.inWholeMilliseconds.toFloat(),
                        targetPollDelay.inWholeMilliseconds.toFloat(),
                        0F,
                    ).toInt().milliseconds
                }
            }
            waitForUpOrCancellation()
            initialTouchHeldJob.cancel()
        }
    }
}

inline fun Modifier.conditional(
    condition: Boolean,
    ifTrue: Modifier.() -> Modifier,
    ifFalse: Modifier.() -> Modifier = { this },
): Modifier = if (condition) {
    then(ifTrue(Modifier))
} else {
    then(ifFalse(Modifier))
}

inline fun <T> Modifier.conditional(
    condition: T?,
    ifNotNull: Modifier.(T) -> Modifier,
    ifNull: Modifier.() -> Modifier = { this },
): Modifier {
    return if (condition != null) {
        then(ifNotNull(Modifier, condition))
    } else {
        then(ifNull(Modifier))
    }
}

fun Modifier.shimmer(
    showShimmer: Boolean = true,
    targetValue: Float = 1000f,
) = this.composed {
    this.then(
        background(
            if (showShimmer) {
                val shimmerColors = listOf(
                    Color.LightGray.copy(alpha = 0.6f),
                    Color.LightGray.copy(alpha = 0.2f),
                    Color.LightGray.copy(alpha = 0.6f),
                )

                val transition = rememberInfiniteTransition(label = "")
                val translateAnimation = transition.animateFloat(
                    initialValue = 0f,
                    targetValue = targetValue,
                    animationSpec = infiniteRepeatable(
                        animation = tween(800),
                        repeatMode = RepeatMode.Reverse,
                    ),
                    label = "",
                )
                Brush.linearGradient(
                    colors = shimmerColors,
                    start = Offset.Zero,
                    end = Offset(x = translateAnimation.value, y = translateAnimation.value),
                )
            } else {
                Brush.linearGradient(
                    colors = listOf(Color.Transparent, Color.Transparent),
                    start = Offset.Zero,
                    end = Offset.Zero,
                )
            },
        ),
    )
}
