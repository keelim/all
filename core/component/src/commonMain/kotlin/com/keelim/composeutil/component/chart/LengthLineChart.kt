package com.keelim.composeutil.component.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp

// 순수 UI용 primitive 모델
data class LengthChartPoint(
    val date: String,
    val value: Float
)

@Composable
fun LengthLineChart(
    points: List<LengthChartPoint>,
    modifier: Modifier = Modifier
) {
    if (points.isEmpty()) return
    val maxLength = points.maxOf { it.value }
    val minLength = points.minOf { it.value }
    val yRange = (maxLength - minLength).takeIf { it > 0 } ?: 1f
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            val stepX = size.width / (points.size - 1).coerceAtLeast(1)
            val stepY = size.height / yRange
            val path = Path()
            points.forEachIndexed { i, point ->
                val x = i * stepX
                val y = size.height - ((point.value - minLength) * stepY)
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            drawPath(path, color = Color.Blue, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f))
            // 점 찍기
            points.forEachIndexed { i, point ->
                val x = i * stepX
                val y = size.height - ((point.value - minLength) * stepY)
                drawCircle(Color.Red, radius = 6f, center = Offset(x, y))
            }
        }
    }
} 