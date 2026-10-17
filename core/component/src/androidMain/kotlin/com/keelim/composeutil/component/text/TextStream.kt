package com.keelim.composeutil.component.text

import android.animation.ValueAnimator
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import com.keelim.core.designsystem.theme.KeelimDesignSystemTheme
import com.keelim.core.designsystem.theme.KuiTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.min
import kotlin.math.pow

/**
 * A vertically looping text reel. Pass either [scrollState] or [lazyListState] from its parent
 * to let scrolling change the reel's direction and speed. With no state it runs at a steady pace.
 */
@Composable
fun TextStream(
    items: List<String>,
    prefix: String,
    modifier: Modifier = Modifier,
    height: Dp = 400.dp,
    textStyle: TextStyle = KuiTheme.typography.displayMedium,
    textColor: Color = KuiTheme.colorScheme.onSurface,
    paused: Boolean = false,
    scrollState: ScrollState? = null,
    lazyListState: LazyListState? = null,
) {
    if (items.isEmpty()) return

    val density = LocalDensity.current
    val spacing = KuiTheme.spacing
    val fontSize = textStyle.fontSize.takeIf { it.isSpecified } ?: KuiTheme.typography.displayMedium.fontSize
    val lineHeight = with(density) { (textStyle.lineHeight.takeIf { it.isSpecified } ?: fontSize * 1.1f).toDp() } + spacing.space3
    val viewportHeight = lineHeight * 2.2f
    val rowHeightPx = with(density) { lineHeight.toPx() }
    val cycleHeightPx = rowHeightPx * items.size
    val copyCount = maxOf(2, ceil(with(density) { viewportHeight.toPx() } / cycleHeightPx).toInt() + 2)
    val reducedMotion = !ValueAnimator.areAnimatorsEnabled()
    var offsetPx by remember(items, rowHeightPx) { mutableFloatStateOf(0f) }
    var targetVelocity by remember(items) { mutableFloatStateOf(0.6f) }
    var currentVelocity by remember(items) { mutableFloatStateOf(0.6f) }
    var direction by remember(items) { mutableIntStateOf(1) }
    var scrollEvent by remember(items) { mutableIntStateOf(0) }

    fun applyScroll(delta: Float) {
        if (delta == 0f) return
        direction = if (delta > 0f) -1 else 1
        targetVelocity = direction * min(12f, 0.6f + abs(delta).pow(1.2f) * 0.08f)
        scrollEvent++
    }

    LaunchedEffect(scrollState, reducedMotion, paused) {
        if (scrollState != null && !reducedMotion && !paused) {
            var previous = scrollState.value
            snapshotFlow { scrollState.value }.collect { position ->
                applyScroll((position - previous).toFloat())
                previous = position
            }
        }
    }
    LaunchedEffect(lazyListState, reducedMotion, paused) {
        if (lazyListState != null && !reducedMotion && !paused) {
            var previous = lazyListState.layoutInfo.visibleItemsInfo.map { it.index to it.offset }
            snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo.map { it.index to it.offset } }
                .collect { visible ->
                    val shared = visible.firstOrNull { current -> previous.any { it.first == current.first } }
                    val delta = if (shared != null) {
                        val oldOffset = previous.first { it.first == shared.first }.second
                        (oldOffset - shared.second).toFloat()
                    } else {
                        ((visible.firstOrNull()?.first ?: 0) - (previous.firstOrNull()?.first ?: 0)).toFloat()
                    }
                    applyScroll(delta)
                    previous = visible
                }
        }
    }
    LaunchedEffect(scrollEvent, paused, reducedMotion) {
        if (!paused && !reducedMotion && scrollEvent > 0) {
            delay(120)
            targetVelocity = direction * 0.6f
        }
    }
    LaunchedEffect(items, paused, reducedMotion, cycleHeightPx) {
        if (paused || reducedMotion || cycleHeightPx <= 0f) return@LaunchedEffect
        var previousFrame = 0L
        while (true) {
            withFrameNanos { frame ->
                if (previousFrame != 0L) {
                    val frameFactor = ((frame - previousFrame) / 1_000_000_000f * 60f).coerceIn(0f, 3f)
                    currentVelocity += (targetVelocity - currentVelocity) * 0.14f
                    offsetPx = ((offsetPx + currentVelocity * frameFactor) % cycleHeightPx + cycleHeightPx) % cycleHeightPx
                }
                previousFrame = frame
            }
        }
    }

    Column(
        modifier = modifier.height(height),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = prefix.uppercase(),
            style = KuiTheme.typography.labelSmall,
            color = textColor.copy(alpha = 0.4f),
        )
        Box(
            modifier = Modifier
                .padding(top = spacing.space2)
                .fillMaxWidth()
                .height(viewportHeight)
                .clipToBounds()
                .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                .drawWithContent {
                    drawContent()
                    if (!reducedMotion) {
                        drawRect(
                            brush = Brush.verticalGradient(
                                0f to Color.Transparent,
                                0.3f to Color.Black,
                                0.7f to Color.Black,
                                1f to Color.Transparent,
                            ),
                            blendMode = BlendMode.DstIn,
                        )
                    }
                }
                .clearAndSetSemantics { contentDescription = items.joinToString(", ") },
            contentAlignment = if (reducedMotion) Alignment.Center else Alignment.TopCenter,
        ) {
            if (reducedMotion) {
                Column {
                    items.take(2).forEach { item -> StreamItem(item, textStyle, textColor, lineHeight) }
                }
            } else {
                Column(
                    modifier = Modifier.graphicsLayer {
                        translationY = offsetPx - cycleHeightPx
                    },
                ) {
                    repeat(copyCount) {
                        items.forEach { item -> StreamItem(item, textStyle, textColor, lineHeight) }
                    }
                }
            }
        }
    }
}

@Composable
private fun StreamItem(text: String, style: TextStyle, color: Color, height: Dp) {
    Box(modifier = Modifier.fillMaxWidth().height(height), contentAlignment = Alignment.Center) {
        Text(text = text, style = style, color = color, textAlign = TextAlign.Center, maxLines = 1)
    }
}

@Preview
@Composable
private fun TextStreamPreview() {
    KeelimDesignSystemTheme {
        TextStream(items = listOf("Create", "Explore", "Build", "Ship"), prefix = "Let's")
    }
}
