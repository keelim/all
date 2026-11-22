package com.keelim.nandadiagnosis.wellness.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.keelim.common.extensions.toUiNumber
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.wellness.WellnessUiState
import com.keelim.model.wellness.DailyCheckIn
import com.keelim.nandadiagnosis.wellness.domain.InsightCalculator
import java.time.LocalDate

internal enum class ConditionMetric { SLEEP, STRESS, ENERGY, DESIRE, CONFIDENCE }

@Composable
internal fun InsightsScreen(
    uiState: WellnessUiState,
    privacyMode: Boolean,
) {
    var periodDays by rememberSaveable { mutableIntStateOf(7) }
    var metric by rememberSaveable { mutableStateOf(ConditionMetric.SLEEP) }
    var showTrendValues by rememberSaveable(privacyMode) { mutableStateOf(false) }
    val today = LocalDate.now()
    val checkInsByDate = remember(uiState.checkIns) { uiState.checkIns.associateBy { it.localDate } }
    val trendDates = remember(today, periodDays) {
        List(periodDays) { index -> today.minusDays((periodDays - 1 - index).toLong()) }
    }
    val trendRecords = remember(trendDates, checkInsByDate) {
        trendDates.map { checkInsByDate[it.toString()] }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp),
    ) {
        item(key = "insightsHeader") {
            Text(
                text = stringResource(R.string.wellness_insights_title),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
        item(key = "recentRecords") {
            InsightSectionCard(title = stringResource(R.string.wellness_recent_records_title)) {
                WellnessRecordHistory(today, checkInsByDate, uiState.careCheckIns, privacyMode)
            }
        }
        item(key = "conditionTrend") {
            InsightSectionCard(title = stringResource(R.string.wellness_condition_trend)) {
                if (privacyMode) {
                    Text(
                        text = stringResource(R.string.wellness_chart_private),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    return@InsightSectionCard
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(7, 28).forEach { days ->
                        FilterChip(
                            selected = periodDays == days,
                            onClick = { periodDays = days },
                            label = {
                                Text(
                                    text = stringResource(
                                        if (days == 7) R.string.wellness_period_seven
                                        else R.string.wellness_period_twenty_eight,
                                    ),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            },
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    ConditionMetric.entries.forEach { option ->
                        FilterChip(
                            selected = metric == option,
                            onClick = { metric = option },
                            label = {
                                Text(
                                    text = metricLabel(option),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            },
                        )
                    }
                }
                val values = trendRecords.map { it?.valueFor(metric) }
                val recordCount = values.count { it != null }
                if (recordCount < 2) {
                    Text(
                        text = stringResource(R.string.wellness_insights_not_enough),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else {
                    ConditionChart(values)
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = WellnessUiFormat.date(trendDates.first()),
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = WellnessUiFormat.date(trendDates.last()),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Text(
                        text = stringResource(
                            R.string.wellness_chart_summary,
                            recordCount.toUiNumber(),
                            metricLabel(metric),
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                if (recordCount > 0) {
                    TextButton(onClick = { showTrendValues = !showTrendValues }) {
                        Text(
                            text = stringResource(
                                if (showTrendValues) R.string.wellness_hide_trend_values
                                else R.string.wellness_show_trend_values,
                            ),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                    if (showTrendValues) {
                        trendDates.zip(values).asReversed().forEach { (date, value) ->
                            WellnessRecordValue(
                                label = WellnessUiFormat.date(date),
                                value = value?.toUiNumber()
                                    ?: stringResource(R.string.wellness_record_missing),
                            )
                        }
                    }
                }
            }
        }
        if (!privacyMode) {
            item(key = "patterns") {
                val insight = InsightCalculator.firstPattern(trendRecords.filterNotNull())
                InsightSectionCard(title = stringResource(R.string.wellness_patterns_title)) {
                    Text(
                        text = if (insight == null) {
                            stringResource(R.string.wellness_patterns_empty)
                        } else {
                            stringResource(
                                R.string.wellness_insight_sleep_energy,
                                insight.sampleDays.toUiNumber(),
                            )
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun InsightSectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            content()
        }
    }
}

@Composable
private fun ConditionChart(values: List<Int?>) {
    val lineColor = MaterialTheme.colorScheme.primary
    val gridColor = MaterialTheme.colorScheme.outlineVariant
    val reveal = remember { Animatable(0f) }
    LaunchedEffect(values) {
        reveal.snapTo(0f)
        reveal.animateTo(
            targetValue = 1f,
            animationSpec = tween(480, easing = FastOutSlowInEasing),
        )
    }
    Canvas(modifier = Modifier.fillMaxWidth().height(180.dp)) {
        val inset = 4.dp.toPx()
        repeat(5) { index ->
            val y = inset + (size.height - 2 * inset) * index / 4f
            drawLine(gridColor, Offset(inset, y), Offset(size.width - inset, y))
        }
        val path = Path()
        val points = mutableListOf<Offset>()
        var previousDayHasValue = false
        values.forEachIndexed { index, value ->
            if (value == null) {
                previousDayHasValue = false
            } else {
                val x = inset + (size.width - 2 * inset) * index / values.lastIndex
                val y = inset + (size.height - 2 * inset) * (5 - value) / 4f
                if (previousDayHasValue) path.lineTo(x, y) else path.moveTo(x, y)
                points.add(Offset(x, y))
                previousDayHasValue = true
            }
        }
        withTransform({ scale(scaleX = reveal.value, scaleY = 1f, pivot = Offset.Zero) }) {
            drawPath(path, lineColor, style = androidx.compose.ui.graphics.drawscope.Stroke(2.dp.toPx()))
            points.forEach { point -> drawCircle(lineColor, radius = 3.dp.toPx(), center = point) }
        }
    }
}

@Composable
internal fun metricLabel(metric: ConditionMetric): String =
    stringResource(
        when (metric) {
            ConditionMetric.SLEEP -> R.string.wellness_condition_sleep
            ConditionMetric.STRESS -> R.string.wellness_condition_stress
            ConditionMetric.ENERGY -> R.string.wellness_condition_energy
            ConditionMetric.DESIRE -> R.string.wellness_condition_desire
            ConditionMetric.CONFIDENCE -> R.string.wellness_condition_confidence
        },
    )

internal fun DailyCheckIn.valueFor(metric: ConditionMetric): Int =
    when (metric) {
        ConditionMetric.SLEEP -> sleep
        ConditionMetric.STRESS -> stress
        ConditionMetric.ENERGY -> energy
        ConditionMetric.DESIRE -> desire
        ConditionMetric.CONFIDENCE -> confidence
    }
