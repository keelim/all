/*
 * Designed and developed by 2021 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keelim.composeutil.component.custom

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.keelim.composeutil.resource.space24
import kotlin.random.Random

@Composable
fun Heart(modifier: Modifier, horizontalPadding: Int, bottomMargin: Int) {
    val w = LocalConfiguration.current.screenWidthDp
    val h = LocalConfiguration.current.screenHeightDp - bottomMargin

    val yRandom = Random.nextInt(0, h / 2)
    val xRandom = Random.nextInt(horizontalPadding, (w - horizontalPadding))

    val state = remember { mutableStateOf(HearState.SELECTED) }
    val offsetYAnimation: Dp by animateDpAsState(
        targetValue = when (state.value) {
            HearState.SELECTED -> h.dp
            else -> yRandom.dp
        },
        animationSpec = tween(1000),
        label = "",
    )

    val offsetXAnimation: Dp by animateDpAsState(
        targetValue = when (state.value) {
            HearState.SELECTED -> (((w - (horizontalPadding * 2)) / 2) + 8).dp
            else -> xRandom.dp
        },
        animationSpec = tween(1000),
        label = "",
    )

    LaunchedEffect(
        key1 = state,
        block = {
            state.value = when (state.value) {
                HearState.SELECTED -> HearState.UNSELECTED
                HearState.UNSELECTED -> HearState.SELECTED
            }
        },
    )

    AnimatedVisibility(
        visible = state.value == HearState.SELECTED,
        enter = fadeIn(animationSpec = tween(durationMillis = 250)),
        exit = fadeOut(animationSpec = tween(durationMillis = 700)),
    ) {
        Canvas(
            modifier = modifier
                .offset(y = offsetYAnimation, x = offsetXAnimation),
            onDraw = {
                val path = Path().apply {
                    heartPath(Size(120f, 120f))
                }
                drawPath(
                    path = path,
                    color = Color.Red,
                )
            },
        )
    }
}

fun Path.heartPath(size: Size): Path {
    // the logic is taken from StackOverFlow [answer](https://stackoverflow.com/a/41251829/5348665)and converted into extension function

    val width: Float = size.width
    val height: Float = size.height

    // Starting point
    moveTo(width / 2, height / 5)

    // Upper left path
    cubicTo(
        5 * width / 14,
        0f,
        0f,
        height / 15,
        width / 28,
        2 * height / 5,
    )

    // Lower left path
    cubicTo(
        width / 14,
        2 * height / 3,
        3 * width / 7,
        5 * height / 6,
        width / 2,
        height,
    )

    // Lower right path
    cubicTo(
        4 * width / 7,
        5 * height / 6,
        13 * width / 14,
        2 * height / 3,
        27 * width / 28,
        2 * height / 5,
    )

    // Upper right path
    cubicTo(
        width,
        height / 15,
        9 * width / 14,
        0f,
        width / 2,
        height / 5,
    )
    return this
}

@Preview
@Composable
fun HeartScreen() {
    val heartCount = remember { mutableIntStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        repeat(heartCount.value) {
            Heart(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 36.dp),
                horizontalPadding = 24,
                bottomMargin = 110,
            )
        }

        Button(
            onClick = {
                heartCount.value++
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(space24)
                .wrapContentHeight()
                .wrapContentWidth(),
        ) {
            Text(
                text = "Like",
                color = Color.White,
            )
        }
    }
}

enum class HearState {
    SELECTED,
    UNSELECTED,
}
