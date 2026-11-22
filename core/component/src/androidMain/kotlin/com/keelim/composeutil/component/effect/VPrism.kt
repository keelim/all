package com.keelim.composeutil.component.effect

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keelim.core.component.R
import com.keelim.core.designsystem.theme.KeelimDesignSystemTheme
import com.keelim.core.designsystem.theme.KuiTheme
import kotlin.math.hypot
import kotlin.math.min

/**
 * A touch-aimed glass-prism illustration inspired by ObsidianUI's V-Prism.
 * Give [modifier] a size; the prism and light scale with the available canvas.
 */
@Composable
fun VPrism(
    modifier: Modifier = Modifier,
    backgroundColor: Color = KuiTheme.colorScheme.surface,
    prismTint: Color = KuiTheme.colorScheme.onSurface,
    spectrumIntensity: Float = 1f,
) {
    var aim by remember { mutableStateOf<Offset?>(null) }
    val description = stringResource(R.string.v_prism_content_description)
    Canvas(
        modifier = modifier
            .clipToBounds()
            .pointerInput(Unit) {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    aim = down.position
                    while (true) {
                        val change = awaitPointerEvent().changes.firstOrNull { it.id == down.id } ?: break
                        if (!change.pressed) break
                        aim = change.position
                        change.consume()
                    }
                }
            }
            .semantics { contentDescription = description },
    ) {
        drawRect(backgroundColor)
        val center = Offset(size.width * 0.5f, size.height * 0.52f)
        val prismSize = min(size.width * 0.38f, size.height * 0.65f)
        val apex = Offset(center.x, center.y - prismSize * 0.64f)
        val left = Offset(center.x - prismSize * 0.5f, center.y + prismSize * 0.43f)
        val right = Offset(center.x + prismSize * 0.5f, left.y)
        val intersection = Offset(center.x, center.y + prismSize * 0.08f)
        val pointer = aim ?: Offset(0f, center.y + prismSize * 0.05f)
        val vx = intersection.x - pointer.x
        val vy = intersection.y - pointer.y
        val length = hypot(vx, vy).coerceAtLeast(1f)
        val direction = Offset(vx / length, vy / length)
        val normal = Offset(-direction.y, direction.x)
        val reach = maxOf(size.width, size.height) * 1.5f
        val source = intersection - direction * reach
        val exit = intersection + direction * (prismSize * 0.34f)
        val beamEnd = exit + direction * reach
        val isLight = backgroundColor.luminance() > 0.5f

        drawSpectrum(exit, beamEnd, normal, prismSize, spectrumIntensity.coerceIn(0f, 3f))

        val beamColor = if (isLight) Color.DarkGray else Color.White
        drawLine(beamColor.copy(alpha = if (isLight) 0.14f else 0.18f), source, intersection, strokeWidth = prismSize * 0.022f)
        drawLine(beamColor.copy(alpha = if (isLight) 0.53f else 0.76f), source, intersection, strokeWidth = prismSize * 0.008f)

        val glass = if (isLight) Color.LightGray else Color.White
        val outer = triangle(apex, right, left)
        drawPath(
            outer,
            brush = Brush.linearGradient(
                0f to glass.copy(alpha = if (isLight) 0.72f else 0.18f),
                0.5f to prismTint.copy(alpha = if (isLight) 0.07f else 0.12f),
                1f to glass.copy(alpha = if (isLight) 0.8f else 0.23f),
                start = left,
                end = right,
            ),
        )
        val facet = Offset(center.x, center.y + prismSize * 0.14f)
        drawPath(
            triangle(apex, facet, left),
            brush = Brush.linearGradient(
                listOf(glass.copy(alpha = 0.32f), prismTint.copy(alpha = 0.03f)),
                start = apex,
                end = left,
            ),
        )
        drawPath(
            triangle(apex, right, facet),
            brush = Brush.linearGradient(
                listOf(Color.White.copy(alpha = 0.03f), glass.copy(alpha = 0.27f)),
                start = apex,
                end = right,
            ),
        )
        val edge = if (isLight) Color.DarkGray.copy(alpha = 0.19f) else Color.White.copy(alpha = 0.56f)
        drawPath(outer, edge, style = Stroke(width = prismSize * 0.006f))
        drawLine(Color.White.copy(alpha = 0.67f), apex, facet, strokeWidth = prismSize * 0.009f)
        drawLine(Color.White.copy(alpha = 0.72f), facet, left, strokeWidth = prismSize * 0.008f)
        drawLine(Color.White.copy(alpha = 0.72f), facet, right, strokeWidth = prismSize * 0.008f)
    }
}

private fun triangle(a: Offset, b: Offset, c: Offset) = Path().apply {
    moveTo(a.x, a.y)
    lineTo(b.x, b.y)
    lineTo(c.x, c.y)
    close()
}

private fun DrawScope.drawSpectrum(
    origin: Offset,
    end: Offset,
    normal: Offset,
    prismSize: Float,
    intensity: Float,
) {
    if (intensity == 0f) return
    val colors = listOf(
        Color(0xFFFF283A), Color(0xFFFF8A24), Color(0xFFFFE14A),
        Color(0xFF56F47A), Color(0xFF33E6F2), Color(0xFF4166FF), Color(0xFFA949FA),
    )
    val width = prismSize * 0.57f
    colors.forEachIndexed { index, color ->
        val fraction = index / (colors.lastIndex).toFloat() - 0.5f
        val outer = end + normal * (fraction * width)
        val bandHalf = width / colors.size * 0.7f
        val band = triangle(origin, outer + normal * bandHalf, outer - normal * bandHalf)
        drawPath(
            band,
            brush = Brush.linearGradient(
                listOf(color.copy(alpha = 0.16f * intensity), color.copy(alpha = 0.72f * intensity.coerceAtMost(1f))),
                start = origin,
                end = outer,
            ),
        )
        drawLine(color.copy(alpha = 0.16f * intensity), origin, outer, strokeWidth = prismSize * 0.06f)
    }
}

@Preview
@Composable
private fun VPrismPreview() {
    KeelimDesignSystemTheme {
        VPrism(modifier = Modifier.fillMaxWidth().height(400.dp))
    }
}
