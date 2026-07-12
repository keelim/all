package com.keelim.composeutil.component.canvas.chart

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.util.fastForEachIndexed

@Stable
data class PieChartEntry(
    val name: String,
    val color: Color,
    val percentage: Float,
)

private fun calculateStartAngles(
    entries: List<PieChartEntry>,
): List<Float> {
    var totalPercentage = 0f
    return entries.map { entry ->
        val startAngle = totalPercentage * 360
        totalPercentage += entry.percentage
        startAngle
    }
}

@Composable
fun PieChart(
    entries: List<PieChartEntry>,
    radiusOuter: Dp,
    chartBarWidth: Dp,
    duration: Int,
    modifier: Modifier = Modifier,
) {
    var isPlayed by remember { mutableStateOf(false) }
    val animatedSweep by
        animateFloatAsState(
            targetValue = if (isPlayed) 1f else 0f,
            animationSpec =
            tween(durationMillis = duration, delayMillis = 0, easing = LinearOutSlowInEasing),
            label = "PieChartSweep",
        )

    LaunchedEffect(key1 = Unit) { isPlayed = true }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                progressBarRangeInfo = ProgressBarRangeInfo(animatedSweep, 0f..1f)
            },
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(radiusOuter * 2f)) {
            val start = calculateStartAngles(entries)
            entries.fastForEachIndexed { index, entry ->
                drawArc(
                    color = entry.color,
                    startAngle = start[index],
                    sweepAngle = entry.percentage * 360f * animatedSweep,
                    useCenter = true,
                    size = this.size,
                    style = Stroke(chartBarWidth.toPx()),
                )
            }
        }
    }
}
