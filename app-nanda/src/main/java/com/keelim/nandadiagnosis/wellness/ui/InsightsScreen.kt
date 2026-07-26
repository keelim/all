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
import androidx.compose.ui.unit.dp
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.wellness.WellnessUiState
import com.keelim.nandadiagnosis.wellness.domain.DailyCheckIn
import com.keelim.nandadiagnosis.wellness.domain.InsightCalculator
import java.time.LocalDate

private enum class ConditionMetric { SLEEP, STRESS, ENERGY, DESIRE, CONFIDENCE }

@Composable
internal fun InsightsScreen(
    uiState: WellnessUiState,
    privacyMode: Boolean,
) {
    var periodDays by rememberSaveable { mutableIntStateOf(7) }
    var metric by rememberSaveable { mutableStateOf(ConditionMetric.SLEEP) }
    val startDate = remember(periodDays) { LocalDate.now().minusDays(periodDays - 1L) }
    val visibleCheckIns = uiState.checkIns.filter {
        runCatching { LocalDate.parse(it.localDate) }.getOrNull()?.let { date ->
            !date.isBefore(startDate)
        } == true
    }
    val insight = InsightCalculator.firstPattern(visibleCheckIns)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp),
    ) {
        item(key = "insightsHeader") {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = stringResource(R.string.wellness_insights_title),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(7, 28).forEach { days ->
                        FilterChip(
                            selected = periodDays == days,
                            onClick = { periodDays = days },
                            label = {
                                Text(
                                    text = stringResource(
                                        if (days == 7) {
                                            R.string.wellness_period_seven
                                        } else {
                                            R.string.wellness_period_twenty_eight
                                        },
                                    ),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            },
                        )
                    }
                }
            }
        }
        item(key = "conditionTrend") {
            InsightSectionCard(title = stringResource(R.string.wellness_condition_trend)) {
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
                if (privacyMode) {
                    Text(
                        text = stringResource(R.string.wellness_chart_private),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else if (visibleCheckIns.size < 2) {
                    Text(
                        text = stringResource(R.string.wellness_insights_not_enough),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else {
                    ConditionChart(
                        values = visibleCheckIns.sortedBy { it.localDate }.map {
                            it.valueFor(metric)
                        },
                    )
                    Text(
                        text = stringResource(
                            R.string.wellness_chart_summary,
                            visibleCheckIns.size,
                            metricLabel(metric),
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
        item(key = "patterns") {
            InsightSectionCard(title = stringResource(R.string.wellness_patterns_title)) {
                Text(
                    text = if (insight == null) {
                        stringResource(R.string.wellness_patterns_empty)
                    } else {
                        stringResource(
                            R.string.wellness_insight_sleep_energy,
                            insight.sampleDays,
                        )
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        item(key = "review") {
            InsightSectionCard(title = stringResource(R.string.wellness_review_title)) {
                Text(
                    text = stringResource(R.string.wellness_review_description),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
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
private fun ConditionChart(values: List<Int>) {
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
        repeat(5) { index ->
            val y = size.height * index / 4f
            drawLine(gridColor, Offset(0f, y), Offset(size.width, y))
        }
        val path = Path()
        values.forEachIndexed { index, value ->
            val x = if (values.size == 1) 0f else size.width * index / (values.size - 1f)
            val y = size.height * (5 - value) / 4f
            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        withTransform({
            scale(
                scaleX = reveal.value,
                scaleY = 1f,
                pivot = Offset.Zero,
            )
        }) {
            drawPath(path, lineColor, style = androidx.compose.ui.graphics.drawscope.Stroke(4f))
        }
    }
}

@Composable
private fun metricLabel(metric: ConditionMetric): String =
    stringResource(
        when (metric) {
            ConditionMetric.SLEEP -> R.string.wellness_condition_sleep
            ConditionMetric.STRESS -> R.string.wellness_condition_stress
            ConditionMetric.ENERGY -> R.string.wellness_condition_energy
            ConditionMetric.DESIRE -> R.string.wellness_condition_desire
            ConditionMetric.CONFIDENCE -> R.string.wellness_condition_confidence
        },
    )

private fun DailyCheckIn.valueFor(metric: ConditionMetric): Int =
    when (metric) {
        ConditionMetric.SLEEP -> sleep
        ConditionMetric.STRESS -> stress
        ConditionMetric.ENERGY -> energy
        ConditionMetric.DESIRE -> desire
        ConditionMetric.CONFIDENCE -> confidence
    }
