package com.keelim.mygrade.ui.screen.analytics

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import com.keelim.core.designsystem.theme.KuiTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.component.layout.Loading
import com.keelim.model.DailyStudyStats
import com.keelim.model.SubjectStudyStats
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun StudyAnalyticsRoute(
    viewModel: StudyAnalyticsViewModel = hiltViewModel(),
) = trace("StudyAnalyticsRoute") {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    StudyAnalyticsScreen(uiState = uiState)
}

@Composable
fun StudyAnalyticsScreen(
    uiState: StudyAnalyticsUiState,
) = trace("StudyAnalyticsScreen") {
    AnimatedVisibility(
        visible = !uiState.isLoading,
        enter = fadeIn(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = "Study Analytics",
                style = KuiTheme.typography.headlineMedium,
                color = KuiTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Summary Cards
            SummarySection(uiState)
            Spacer(modifier = Modifier.height(24.dp))

            // Heatmap
            HeatmapSection(dailyStats = uiState.dailyStats)
            Spacer(modifier = Modifier.height(24.dp))

            // Weekly Chart
            WeeklyChartSection(dailyStats = uiState.dailyStats)
            Spacer(modifier = Modifier.height(24.dp))

            // Subject Distribution
            SubjectDistributionSection(subjectStats = uiState.subjectStats)
        }
    }

    if (uiState.isLoading) {
        Loading()
    }
}

@Composable
private fun SummarySection(
    uiState: StudyAnalyticsUiState,
) = trace("SummarySection") {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SummaryCard(
            modifier = Modifier.weight(1f),
            title = "Total Time",
            value = formatSeconds(uiState.totalSeconds),
            color = KuiTheme.colorScheme.primaryContainer,
        )
        SummaryCard(
            modifier = Modifier.weight(1f),
            title = "Study Days",
            value = "${uiState.studyDaysCount}",
            color = KuiTheme.colorScheme.secondaryContainer,
        )
        SummaryCard(
            modifier = Modifier.weight(1f),
            title = "Streak 🔥",
            value = "${uiState.currentStreak}",
            color = KuiTheme.colorScheme.tertiaryContainer,
        )
    }
}

@Composable
private fun SummaryCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier,
) = trace("SummaryCard") {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                style = KuiTheme.typography.labelMedium,
                color = KuiTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = KuiTheme.typography.titleLarge,
                color = KuiTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun HeatmapSection(
    dailyStats: List<DailyStudyStats>,
) = trace("HeatmapSection") {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = KuiTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Activity Heatmap",
                    style = KuiTheme.typography.titleMedium,
                    color = KuiTheme.colorScheme.onSurface,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            HeatmapGrid(dailyStats = dailyStats)
            Spacer(modifier = Modifier.height(8.dp))
            HeatmapLegend()
        }
    }
}

@Composable
private fun HeatmapGrid(
    dailyStats: List<DailyStudyStats>,
) = trace("HeatmapGrid") {
    val statsMap = dailyStats.associate { it.date to it.totalSeconds }
    val weeks = 12
    val daysPerWeek = 7

    Column(
        verticalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        repeat(daysPerWeek) { dayIndex ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                repeat(weeks) { weekIndex ->
                    val dayOffset = weekIndex * 7 + dayIndex
                    val date = kotlinx.datetime.Clock.System.now()
                        .minus(kotlin.time.Duration.parse("${(weeks * 7 - dayOffset)}d"))
                        .let {
                            TimeZone.currentSystemDefault()
                                .let { tz -> it.toLocalDateTime(tz).date.toString() }
                        }
                    val seconds = statsMap[date] ?: 0
                    val intensity = getHeatmapIntensity(seconds)
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(intensity),
                    )
                }
            }
        }
    }
}

@Composable
private fun HeatmapLegend() = trace("HeatmapLegend") {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Less",
            style = KuiTheme.typography.labelSmall,
            color = KuiTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.width(4.dp))
        listOf(
            KuiTheme.colorScheme.surfaceVariant,
            KuiTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
            KuiTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
            KuiTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
            KuiTheme.colorScheme.primary,
        ).forEach { color ->
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(color),
            )
            Spacer(modifier = Modifier.width(2.dp))
        }
        Text(
            text = "More",
            style = KuiTheme.typography.labelSmall,
            color = KuiTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun getHeatmapIntensity(seconds: Int): Color {
    val minutes = seconds / 60
    return when {
        seconds == 0 -> KuiTheme.colorScheme.surfaceVariant
        minutes < 30 -> KuiTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        minutes < 60 -> KuiTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
        minutes < 120 -> KuiTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
        else -> KuiTheme.colorScheme.primary
    }
}

@Composable
fun WeeklyChartSection(
    dailyStats: List<DailyStudyStats>,
) = trace("WeeklyChartSection") {
    val recentDays = dailyStats.take(7).reversed()
    val maxSeconds = recentDays.maxOfOrNull { it.totalSeconds } ?: 1

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = "Weekly Overview",
                style = KuiTheme.typography.titleMedium,
                color = KuiTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom,
            ) {
                if (recentDays.isEmpty()) {
                    Text(
                        text = "No data yet",
                        style = KuiTheme.typography.bodyMedium,
                        color = KuiTheme.colorScheme.onSurfaceVariant,
                    )
                } else {
                    recentDays.forEach { stat ->
                        val height = (stat.totalSeconds.toFloat() / maxSeconds * 100).dp
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(24.dp)
                                    .height(height.coerceAtLeast(4.dp))
                                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                    .background(KuiTheme.colorScheme.primary),
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stat.date.takeLast(2),
                                style = KuiTheme.typography.labelSmall,
                                color = KuiTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SubjectDistributionSection(
    subjectStats: List<SubjectStudyStats>,
) = trace("SubjectDistributionSection") {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = "Subject Distribution",
                style = KuiTheme.typography.titleMedium,
                color = KuiTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (subjectStats.isEmpty()) {
                Text(
                    text = "No subjects recorded yet",
                    style = KuiTheme.typography.bodyMedium,
                    color = KuiTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                val totalSeconds = subjectStats.sumOf { it.totalSeconds }
                subjectStats.take(5).forEach { stat ->
                    val percentage = if (totalSeconds > 0) {
                        stat.totalSeconds.toFloat() / totalSeconds
                    } else {
                        0f
                    }
                    SubjectProgressBar(
                        subject = stat.subject.ifEmpty { "General" },
                        percentage = percentage,
                        time = formatSeconds(stat.totalSeconds),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun SubjectProgressBar(
    subject: String,
    percentage: Float,
    time: String,
) = trace("SubjectProgressBar") {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = subject,
                style = KuiTheme.typography.bodyMedium,
                color = KuiTheme.colorScheme.onSurface,
            )
            Text(
                text = time,
                style = KuiTheme.typography.bodySmall,
                color = KuiTheme.colorScheme.onSurfaceVariant,
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(KuiTheme.colorScheme.surfaceVariant),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percentage)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(KuiTheme.colorScheme.primary),
            )
        }
    }
}

private fun formatSeconds(seconds: Int): String {
    val hours = seconds / 3600
    val mins = (seconds % 3600) / 60
    val secs = seconds % 60
    return when {
        hours > 0 && mins > 0 -> "${hours}h ${mins}m"
        hours > 0 -> "${hours}h"
        mins > 0 && secs > 0 -> "${mins}m ${secs}s"
        mins > 0 -> "${mins}m"
        else -> "${secs}s"
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewStudyAnalyticsScreen() {
    StudyAnalyticsScreen(
        uiState = StudyAnalyticsUiState(
            dailyStats = listOf(
                DailyStudyStats("2024-01-01", 3600),
                DailyStudyStats("2024-01-02", 7200),
            ),
            subjectStats = listOf(
                SubjectStudyStats("Math", 7200),
                SubjectStudyStats("Science", 5400),
            ),
            totalSeconds = 12600,
            studyDaysCount = 5,
            currentStreak = 3,
            isLoading = false,
        ),
    )
}
