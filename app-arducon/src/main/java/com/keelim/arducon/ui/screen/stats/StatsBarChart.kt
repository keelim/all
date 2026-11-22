package com.keelim.arducon.ui.screen.stats

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.keelim.common.extensions.formatUiDate
import com.keelim.common.extensions.toUiNumber
import com.keelim.core.designsystem.component.KuiText
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.model.UsageStat
import java.time.LocalDate
import java.time.format.DateTimeParseException

@Composable
fun StatsBarChart(stats: List<UsageStat>) {
    if (stats.isEmpty()) {
        KuiText("데이터 없음", style = KuiTheme.typography.bodyMedium, color = KuiTheme.colorScheme.onSurface)
        return
    }

    val maxCount = stats.maxOf { it.count }.coerceAtLeast(1)
    val ticks = listOf(0, maxCount / 2, maxCount).distinct()
    val days = stats.map { stat ->
        try {
            val date = LocalDate.parse(stat.day)
            formatUiDate(date.year, date.monthValue, date.dayOfMonth)
        } catch (_: DateTimeParseException) {
            stat.day
        }
    }
    val counts = stats.map { it.count.toUiNumber() }
    val colors = KuiTheme.colorScheme
    val labelStyle = KuiTheme.typography.labelSmall.copy(color = colors.onSurfaceVariant)
    val countStyle = KuiTheme.typography.labelMedium.copy(color = colors.onSurface)
    val textMeasurer = rememberTextMeasurer(cacheSize = stats.size * 2 + ticks.size)
    val tickText = ticks.map { textMeasurer.measure(it.toUiNumber(), labelStyle) }
    val dayText = days.map { textMeasurer.measure(it, labelStyle) }
    val countText = counts.map { textMeasurer.measure(it, countStyle) }

    val tickWidth = tickText.maxOf { it.size.width }
    val dayHeight = dayText.maxOf { it.size.height }
    val headroom = maxOf(countText.maxOf { it.size.height }, tickText.last().size.height / 2)
    val labelWidth = maxOf(dayText.maxOf { it.size.width }, countText.maxOf { it.size.width })
    val minimumWidth = with(LocalDensity.current) {
        tickWidth.toDp() + 8.dp + (labelWidth.toDp() + 8.dp) * stats.size
    }
    val animation = remember(stats) { Animatable(0f) }
    LaunchedEffect(animation) {
        animation.animateTo(1f, tween())
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .widthIn(min = minimumWidth)
            .height(220.dp)
            .semantics {
                contentDescription = stats.indices.joinToString("\n") { index ->
                    "${days[index]}, ${counts[index]}"
                }
            },
    ) {
        val labelGap = 4.dp.toPx()
        val axisGutter = tickWidth + 8.dp.toPx()
        val isLtr = layoutDirection == LayoutDirection.Ltr
        val axisX = if (isLtr) axisGutter else size.width - axisGutter
        val plotEnd = if (isLtr) size.width else 0f
        val plotTop = headroom + labelGap
        val plotBottom = size.height - dayHeight - labelGap
        val plotHeight = (plotBottom - plotTop).coerceAtLeast(0f)
        val slotStep = (plotEnd - axisX) / stats.size
        val barWidth = (size.width - axisGutter) / stats.size * 0.6f
        val stroke = 1.dp.toPx()

        ticks.forEachIndexed { index, tick ->
            val label = tickText[index]
            val y = plotBottom - plotHeight * (tick.toFloat() / maxCount)
            val labelX = if (isLtr) (tickWidth - label.size.width).toFloat() else size.width - tickWidth
            val lineColor = if (tick == 0) colors.outline else colors.outlineVariant
            drawLine(lineColor, Offset(axisX, y), Offset(plotEnd, y), strokeWidth = stroke)
            drawText(label, topLeft = Offset(labelX, y - label.size.height / 2f))
        }
        drawLine(colors.outline, Offset(axisX, plotTop), Offset(axisX, plotBottom), strokeWidth = stroke)

        val animatedHeight = plotHeight * animation.value
        stats.forEachIndexed { index, stat ->
            val center = axisX + slotStep * (index + 0.5f)
            val barHeight = animatedHeight * (stat.count.coerceAtLeast(0).toFloat() / maxCount)
            val barTop = plotBottom - barHeight
            drawRect(colors.primary, Offset(center - barWidth / 2f, barTop), Size(barWidth, barHeight))

            val count = countText[index]
            drawText(count, topLeft = Offset(center - count.size.width / 2f, barTop - count.size.height - labelGap))
            val day = dayText[index]
            drawText(day, topLeft = Offset(center - day.size.width / 2f, plotBottom + labelGap))
        }
    }
}
