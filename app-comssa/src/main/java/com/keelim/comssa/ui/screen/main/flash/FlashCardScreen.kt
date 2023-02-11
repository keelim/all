
package com.keelim.comssa.ui.screen.main.flash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


sealed class FlashCardState(val angle: Float) {
    abstract fun nextFace(): FlashCardState
    object Front : FlashCardState(angle = 0f) {
        override fun nextFace(): FlashCardState = Back
    }
    object Back: FlashCardState(angle = 180f) {
        override fun nextFace(): FlashCardState = Front
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashCard(
    flashCardState: FlashCardState,
    modifier: Modifier = Modifier,
    back: @Composable () -> Unit = {},
    front: @Composable () -> Unit = {},
    onClick: ((FlashCardState) -> Unit)?,
) {
    val rotation = animateFloatAsState(
        targetValue = flashCardState.angle, animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing,
        )
    )
    Card(
        onClick = { onClick?.invoke(flashCardState) },
        modifier = modifier.graphicsLayer {
            rotationY = rotation.value
            cameraDistance = 12f * density
        },
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (rotation.value <= 90f) {
                Box(
                    Modifier.fillMaxSize()
                ) {
                    front()
                }
            } else {
                Box(
                    Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            rotationY = 180f
                        },
                ) {
                    back()
                }
            }
        }
    }
}

@Composable
fun FlashCardScreen() {
    var cardFace: FlashCardState by remember {
        mutableStateOf(FlashCardState.Front)
    }

    FlashCard(
        flashCardState = cardFace,
        modifier = Modifier
            .fillMaxWidth(.5f)
            .aspectRatio(1f)
            .padding(horizontal = 12.dp),
        front = { FrontScreen() },
        back = { BackScreen() },
        onClick = { cardFace = cardFace.nextFace() },
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FlipCardScreenPreview() {
    FlashCardScreen()
}

@Composable
fun FrontScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Front",
            color = Color.White,
            fontSize = 32.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FrontScreenPreview() {
    FrontScreen()
}

@Composable
fun BackScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Back",
            color = Color.White,
            fontSize = 32.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BackScreenPreview() {
    BackScreen()
}


