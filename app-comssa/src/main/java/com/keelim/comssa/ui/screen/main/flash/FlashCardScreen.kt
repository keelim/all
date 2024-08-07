package com.keelim.comssa.ui.screen.main.flash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.trace
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.resource.space12

@Composable
fun FlashCardRoute(
    viewModel: FlashCardViewModel = hiltViewModel(),
) = trace("FlashCardRoute") {
    val cardFace by viewModel.uiState.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current,
    )
    FlashCardScreen(
        cardFace.flashCardState,
        onClick = viewModel::updateState,
    )
}

@Composable
fun FlashCardScreen(
    flashCardState: FlashCardState,
    onClick: () -> Unit,
) = trace("FlashCardScreen") {
    FlashCard(
        flashCardState = flashCardState,
        modifier = Modifier
            .fillMaxWidth(.5f)
            .aspectRatio(1f)
            .padding(horizontal = space12),
        front = { FrontCardSection() },
        back = { BackCardSection() },
        onClick = onClick,
    )
}

@Composable
fun FlashCard(
    flashCardState: FlashCardState,
    back: @Composable () -> Unit,
    front: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) = trace("FlashCard") {
    val rotation = animateFloatAsState(
        targetValue = flashCardState.angle,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing,
        ),
        label = "",
    )
    Card(
        onClick = onClick,
        modifier = modifier.graphicsLayer {
            rotationY = rotation.value
            cameraDistance = 12f * density
        },
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            if (rotation.value <= 90f) {
                Box(
                    Modifier.fillMaxSize(),
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

@Preview
@Composable
fun PreviewFlashCardScreen() {
    FlashCardScreen(
        flashCardState = FlashCardState.Front,
        onClick = {},
    )
}
