package com.keelim.composeutil.util

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FloatAnimationSpec
import androidx.compose.animation.core.FloatTweenSpec
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
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
) = composed {
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
    onTouchHeld: () -> Unit
) = composed {
    val scope = rememberCoroutineScope()
    pointerInput(onTouchHeld) {
        val animationSpec: FloatAnimationSpec = FloatTweenSpec(
            animationDuration.inWholeSeconds.toInt(),
            0,
            easing
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
                        0F
                    ).toInt().milliseconds
                }
            }
            waitForUpOrCancellation()
            initialTouchHeldJob.cancel()
        }
    }
}
