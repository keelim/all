package com.keelim.composeutil.component.canvas.chart

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keelim.composeutil.util.randomColor

@Preview
@Composable
fun PreviewPieChart() {
    PieChart(
        listOf(
            PieChartEntry(name = "1", randomColor(), 0.5f),
            PieChartEntry(name = "1", randomColor(), 0.3f),
            PieChartEntry(name = "1", randomColor(), 0.2f),
        ),
        radiusOuter = 20.dp,
        chartBarWidth = 20.dp,
        duration = 5892,
    )
}
