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
    mainColor: Color,
    subColor: Color,
    points: List<LengthChartPoint>,
    modifier: Modifier = Modifier
) {
    if (points.isEmpty()) return
    val maxLength = points.maxOf { it.value }
    val minLength = points.minOf { it.value }
    val yRange = (maxLength - minLength).takeIf { it > 0 } ?: 1f
    val labelCount = 4

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxWidth().height(220.dp)) {
            val stepX = size.width / (points.size - 1).coerceAtLeast(1)
            val stepY = size.height / yRange

            // y축 그리드라인만 (라벨 없음)
            for (i in 0..labelCount) {
                val y = size.height - (i * size.height / labelCount)
                drawLine(
                    color = Color.LightGray,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1f
                )
            }

            // 곡선 Path
            val path = Path()
            points.forEachIndexed { i, point ->
                val x = i * stepX
                val y = size.height - ((point.value - minLength) * stepY)
                if (i == 0) path.moveTo(x, y)
                else path.lineTo(x, y)
            }
            drawPath(
                path,
                color = mainColor,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 6f)
            )

            // 점 강조
            points.forEachIndexed { i, point ->
                val x = i * stepX
                val y = size.height - ((point.value - minLength) * stepY)
                drawCircle(
                    color = if (i == points.lastIndex) Color.Red else Color.White,
                    radius = if (i == points.lastIndex) 10f else 7f,
                    center = Offset(x, y),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3f)
                )
                drawCircle(
                    color = subColor,
                    radius = if (i == points.lastIndex) 7f else 5f,
                    center = Offset(x, y)
                )
            }
        }
    }
}
