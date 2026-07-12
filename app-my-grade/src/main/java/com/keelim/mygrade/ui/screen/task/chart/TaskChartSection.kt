package com.keelim.mygrade.ui.screen.task.chart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.designsystem.component.KuiText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.trace
import com.keelim.composeutil.component.canvas.chart.PieChart
import com.keelim.composeutil.component.canvas.chart.PieChartEntry
import com.keelim.composeutil.component.layout.EmptyView
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space24

@Composable
fun TaskChartSuccessSection(
    entries: List<PieChartEntry>,
) = trace("TaskChartSuccessSection") {
    if (entries.isEmpty()) {
        EmptyView()
    } else {
        Column(
            modifier = Modifier.padding(
                vertical = space12,
            ),
        ) {
            KuiText(
                modifier = Modifier.padding(start = 15.dp),
                text = "Task percentage",
                style = KuiTheme.typography.titleLarge,
                color = KuiTheme.colorScheme.onSurface,
            )
            Spacer(
                modifier = Modifier.height(space24),
            )
            PieChart(
                entries = entries,
                radiusOuter = 150.dp,
                chartBarWidth = 20.dp,
                duration = 300,
            )
            Spacer(
                modifier = Modifier.height(space24),
            )
            entries.fastForEach { entry ->
                TaskChartDetailEntry(
                    title = entry.name,
                    color = entry.color,
                )
            }
        }
    }
}
