package com.keelim.composeutil.component.button

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.keelim.composeutil.resource.space4

@Composable
fun SwitchButton(
    width: Dp,
    height: Dp,
    padding: Dp,
    isEnable: Boolean,
) {
    val switchSize by remember {
        mutableStateOf(height - padding * 2)
    }

    val interactionSource = remember {
        MutableInteractionSource()
    }

    var enable by remember {
        mutableStateOf(isEnable)
    }

    var paddingValue by remember {
        mutableStateOf(0.dp)
    }

    paddingValue = if (enable) {
        width - switchSize - padding * 2
    } else {
        0.dp
    }

    val animateSize by animateDpAsState(
        targetValue = if (enable) padding else 0.dp,
        tween(
            durationMillis = 700,
            delayMillis = 0,
            easing = LinearOutSlowInEasing,
        ),
        label = "",
    )

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(CircleShape)
            .background(
                if (enable) {
                    Color.Black
                } else {
                    Color.White
                },
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
            ) {
                enable = !enable
            },
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(animateSize)
                    .background(Color.Transparent),
            )

            Box(
                modifier = Modifier
                    .size(switchSize)
                    .clip(CircleShape)
                    .background(Color.Green),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SwitchButtonPreview() {
    SwitchButton(
        width = 100.dp,
        height = 30.dp,
        padding = space4,
        isEnable = false,
    )
}
