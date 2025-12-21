package com.keelim.nandadiagnosis.ui.screen.water

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.model.DailyWaterTotal

@Composable
fun WaterIntakeRoute(
    viewModel: WaterIntakeViewModel = hiltViewModel(),
) = trace("WaterIntakeRoute") {
    val todayTotal by viewModel.todayTotal.collectAsStateWithLifecycle()
    val dailyGoal by viewModel.dailyGoal.collectAsStateWithLifecycle()
    val todayRecords by viewModel.todayRecords.collectAsStateWithLifecycle()
    val weeklyHistory by viewModel.weeklyHistory.collectAsStateWithLifecycle()

    WaterIntakeScreen(
        todayTotal = todayTotal,
        dailyGoal = dailyGoal,
        todayRecords = todayRecords,
        weeklyHistory = weeklyHistory,
        onAddWater = viewModel::addWaterIntake,
        onDeleteRecord = viewModel::deleteWaterIntake,
    )
}

@Composable
fun WaterIntakeScreen(
    todayTotal: Int,
    dailyGoal: Int,
    todayRecords: List<WaterIntakeUiModel>,
    weeklyHistory: List<DailyWaterTotal>,
    onAddWater: (Int) -> Unit,
    onDeleteRecord: (Long) -> Unit,
) = trace("WaterIntakeScreen") {
    val progress = (todayTotal.toFloat() / dailyGoal).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 500),
        label = "progress",
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // 헤더
        item {
            Text(
                text = "💧 수분 섭취",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
            )
        }

        // 진행률 링
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                ),
                shape = RoundedCornerShape(24.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    ProgressRing(
                        progress = animatedProgress,
                        current = todayTotal,
                        goal = dailyGoal,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (todayTotal >= dailyGoal) "🎉 목표 달성!" else "오늘 목표량의 ${(progress * 100).toInt()}%",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }

        // 빠른 추가 버튼
        item {
            Text(
                text = "빠른 추가",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            QuickAddButtons(onAddWater = onAddWater)
        }

        // 주간 요약
        if (weeklyHistory.isNotEmpty()) {
            item {
                Text(
                    text = "주간 기록",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(8.dp))
                WeeklyChart(history = weeklyHistory, dailyGoal = dailyGoal)
            }
        }

        // 오늘 기록 리스트
        if (todayRecords.isNotEmpty()) {
            item {
                Text(
                    text = "오늘 기록",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            items(todayRecords, key = { it.id }) { record ->
                WaterRecordItem(
                    record = record,
                    onDelete = { onDeleteRecord(record.id) },
                )
            }
        }
    }
}

@Composable
private fun ProgressRing(
    progress: Float,
    current: Int,
    goal: Int,
) = trace("ProgressRing") {
    val primaryColor = MaterialTheme.colorScheme.primary
    val trackColor = MaterialTheme.colorScheme.surfaceVariant
    val completedColor = Color(0xFF4CAF50)

    val ringColor by animateColorAsState(
        targetValue = if (progress >= 1f) completedColor else primaryColor,
        animationSpec = tween(durationMillis = 300),
        label = "ringColor",
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(200.dp),
    ) {
        Canvas(modifier = Modifier.size(180.dp)) {
            val strokeWidth = 16.dp.toPx()
            val radius = (size.minDimension - strokeWidth) / 2
            val center = Offset(size.width / 2, size.height / 2)

            // 트랙
            drawCircle(
                color = trackColor,
                radius = radius,
                center = center,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            )

            // 진행률
            drawArc(
                color = ringColor,
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                size = Size(size.width - strokeWidth, size.height - strokeWidth),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = WaterDropIcon,
                contentDescription = null,
                tint = ringColor,
                modifier = Modifier.size(32.dp),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${current}ml",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "/ ${goal}ml",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun QuickAddButtons(
    onAddWater: (Int) -> Unit,
) = trace("QuickAddButtons") {
    val amounts = listOf(100, 200, 250, 500)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        amounts.forEach { amount ->
            FilledTonalButton(
                onClick = { onAddWater(amount) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
            ) {
                Text(
                    text = "${amount}ml",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
        }
    }
}

@Composable
private fun WeeklyChart(
    history: List<DailyWaterTotal>,
    dailyGoal: Int,
) = trace("WeeklyChart") {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        ),
        shape = RoundedCornerShape(16.dp),
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(history.reversed()) { daily ->
                DailyBar(
                    date = daily.date,
                    amount = daily.totalAmount,
                    goal = dailyGoal,
                )
            }
        }
    }
}

@Composable
private fun DailyBar(
    date: String,
    amount: Int,
    goal: Int,
) = trace("DailyBar") {
    val progress = (amount.toFloat() / goal).coerceIn(0f, 1f)
    val barColor = if (progress >= 1f) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(40.dp),
    ) {
        Box(
            modifier = Modifier
                .height(80.dp)
                .width(24.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp * progress)
                    .clip(RoundedCornerShape(12.dp))
                    .background(barColor),
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = date.takeLast(5).replace("-", "/"),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun WaterRecordItem(
    record: WaterIntakeUiModel,
    onDelete: () -> Unit,
) = trace("WaterRecordItem") {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(40.dp),
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Icon(
                            imageVector = WaterDropIcon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "${record.amount}ml",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        text = record.formattedTime,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "삭제",
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewWaterIntakeScreen() {
    WaterIntakeScreen(
        todayTotal = 1200,
        dailyGoal = 2000,
        todayRecords = listOf(
            WaterIntakeUiModel(id = 1, amount = 200, formattedTime = "10:30"),
            WaterIntakeUiModel(id = 2, amount = 500, formattedTime = "09:15"),
        ),
        weeklyHistory = listOf(
            DailyWaterTotal("2024-01-01", 1200),
            DailyWaterTotal("2023-12-31", 2100),
            DailyWaterTotal("2023-12-30", 1800),
        ),
        onAddWater = {},
        onDeleteRecord = {},
    )
}
