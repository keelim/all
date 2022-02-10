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
package com.keelim.ui_setting.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keelim.ui_setting.ui.theme.Pink80
import com.keelim.ui_setting.ui.theme.Purple80
import kotlinx.coroutines.delay
import java.util.Calendar

data class Time(val hours: Int, val minutes: Int, val seconds: Int)

@Composable
@Preview
fun ClockScreen() {
    fun currentTime(): Time {
        val cal = Calendar.getInstance()
        return Time(
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            cal.get(Calendar.SECOND),
        )
    }

    var time by remember { mutableStateOf(currentTime()) }
    LaunchedEffect(0) {
        while (true) {
            time = currentTime()
            delay(1000)
        }
    }
    Clock(time)
}

@Composable
fun Clock(time: Time) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val padding = Modifier.padding(horizontal = 4.dp)
        NumberColumn(time.hours / 10, 0..2, padding)
        NumberColumn(time.hours % 10, 0..9, padding)

        Spacer(Modifier.size(16.dp))

        NumberColumn(time.minutes / 10, 0..5, padding)
        NumberColumn(time.minutes % 10, 0..9, padding)

        Spacer(Modifier.size(16.dp))

        NumberColumn(time.seconds / 10, 0..5, padding)
        NumberColumn(time.seconds % 10, 0..9, padding)
    }
}

@Composable
fun NumberColumn(
    current: Int,
    range: IntRange,
    modifier: Modifier = Modifier,
) {
    val size = 40.dp

    val mid = (range.last - range.first) / 2f
    val reset = current == range.first
    val offset by animateDpAsState(
        targetValue = size * (mid - current),
        animationSpec = if (reset) {
            spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow,
            )
        } else {
            tween(
                durationMillis = 300,
                easing = LinearOutSlowInEasing,
            )
        }
    )

    Column(
        modifier
            .offset(y = offset)
            .clip(RoundedCornerShape(percent = 25))
    ) {
        range.forEach { num ->
            Number(num == current, num, Modifier.size(size))
        }
    }
}

@Composable
fun Number(active: Boolean, value: Int, modifier: Modifier = Modifier) {
    val backgroundColor by animateColorAsState(
        if (active) Purple80 else Pink80,
    )

    Box(
        modifier = modifier.background(backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = value.toString(),
            fontSize = 20.sp,
            color = Color.White,
        )
    }
}
