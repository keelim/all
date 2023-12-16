package com.keelim.composeutil.component.canvas.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.keelim.composeutil.util.randomColor

@Stable
data class PieChartEntry(
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
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier.size(300.dp),
    ) {
        val start = calculateStartAngles(entries)
        entries.fastForEachIndexed { index, entry ->
            drawArc(
                color = entry.color,
                startAngle = start[index],
                sweepAngle = entry.percentage * 360f,
                useCenter = true,
                topLeft = Offset.Zero,
                size = this.size,
            )
        }
    }
}

@Preview
@Composable
fun PreviewPieChart() {
    PieChart(
        listOf(
            PieChartEntry(randomColor(), 0.5f),
            PieChartEntry(randomColor(), 0.3f),
            PieChartEntry(randomColor(), 0.2f),
        ),
    )
}
