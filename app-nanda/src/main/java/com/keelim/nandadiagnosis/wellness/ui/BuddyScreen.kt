package com.keelim.nandadiagnosis.wellness.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.keelim.model.wellness.Measurement
import com.keelim.model.wellness.WellnessGoal
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.wellness.GoalMetric
import com.keelim.nandadiagnosis.wellness.domain.MeasurementState
import com.keelim.nandadiagnosis.wellness.domain.WellnessRules
import java.time.LocalDate

private enum class ChartRange { WEEK, MONTH }

@Composable
internal fun BuddyScreen(
    measurements: List<Measurement>,
    goal: WellnessGoal?,
    hasValidationError: Boolean,
    hasGoalValidationError: Boolean,
    privacyMode: Boolean,
    onSaveMeasurement: (String, String, MeasurementState) -> Boolean,
    onSetGoal: (GoalMetric, String) -> Boolean,
    onClearGoal: () -> Unit,
) {
    var chartRange by rememberSaveable { mutableStateOf(ChartRange.WEEK) }
    var selectedMetric by rememberSaveable(goal?.metric) {
        mutableStateOf(goal?.metric?.toGoalMetric() ?: GoalMetric.LENGTH)
    }
    var showMeasurementSheet by rememberSaveable { mutableStateOf(false) }
    var showGoalSheet by rememberSaveable { mutableStateOf(false) }
    var showRulerMode by rememberSaveable { mutableStateOf(false) }
    val today = LocalDate.now()
    val visibleMeasurements = measurements.filter { measurement ->
        runCatching { LocalDate.parse(measurement.localDate) }
            .getOrNull()
            ?.isAfter(today.minusDays(if (chartRange == ChartRange.WEEK) 7 else 30)) == true
    }.sortedBy { it.localDate }
    val latest = measurements.maxByOrNull { it.localDate }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        item(key = "buddy_heading") {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = stringResource(R.string.wellness_buddy_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    text = stringResource(R.string.wellness_buddy_intro),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        item(key = "goal") {
            GoalCard(
                goal = goal,
                latest = latest,
                privacyMode = privacyMode,
                onSetGoal = { showGoalSheet = true },
                onClearGoal = onClearGoal,
            )
        }
        item(key = "chart_controls") {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = stringResource(R.string.wellness_chart_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    GoalMetric.entries.forEach { metric ->
                        FilterChip(
                            selected = selectedMetric == metric,
                            onClick = { selectedMetric = metric },
                            label = { MetricText(metric) },
                        )
                    }
                    FilterChip(
                        selected = chartRange == ChartRange.WEEK,
                        onClick = { chartRange = ChartRange.WEEK },
                        label = { Text(stringResource(R.string.wellness_chart_week), style = MaterialTheme.typography.labelLarge) },
                    )
                    FilterChip(
                        selected = chartRange == ChartRange.MONTH,
                        onClick = { chartRange = ChartRange.MONTH },
                        label = { Text(stringResource(R.string.wellness_chart_month), style = MaterialTheme.typography.labelLarge) },
                    )
                }
            }
        }
        item(key = "chart") {
            MeasurementChart(
                measurements = visibleMeasurements,
                metric = selectedMetric,
                privacyMode = privacyMode,
            )
        }
        item(key = "measurement_actions") {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { showRulerMode = true },
                    modifier = Modifier.weight(1f).heightIn(min = 56.dp),
                ) {
                    Text(stringResource(R.string.wellness_ruler_mode), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onPrimary)
                }
                Button(
                    onClick = { showMeasurementSheet = true },
                    modifier = Modifier.weight(1f).heightIn(min = 56.dp),
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                    Text(
                        text = stringResource(R.string.wellness_measurement_add),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(start = 6.dp),
                    )
                }
            }
        }
    }

    if (showMeasurementSheet) {
        MeasurementSheet(
            hasValidationError = hasValidationError,
            privacyMode = privacyMode,
            onDismiss = { showMeasurementSheet = false },
            onSave = onSaveMeasurement,
        )
    }
    if (showGoalSheet) {
        GoalSheet(
            initialMetric = goal?.metric?.toGoalMetric() ?: selectedMetric,
            initialTarget = goal?.targetCm?.let(WellnessUiFormat::number).orEmpty(),
            hasValidationError = hasGoalValidationError,
            hasMeasurements = latest != null,
            onDismiss = { showGoalSheet = false },
            onSave = { metric, target ->
                if (onSetGoal(metric, target)) showGoalSheet = false
            },
        )
    }
    if (showRulerMode) {
        RulerMode(
            onClose = { showRulerMode = false },
            onAddMeasurement = {
                showRulerMode = false
                showMeasurementSheet = true
            },
        )
    }
}

@Composable
private fun RulerMode(
    onClose: () -> Unit,
    onAddMeasurement: () -> Unit,
) {
    var scaleAdjustment by rememberSaveable { mutableStateOf(1f) }
    val metrics = LocalContext.current.resources.displayMetrics
    val pixelsPerMillimeter = (metrics.ydpi / 25.4f) * scaleAdjustment
    val textMeasurer = rememberTextMeasurer()
    val rulerColor = MaterialTheme.colorScheme.primary
    val minorTickColor = MaterialTheme.colorScheme.outlineVariant
    val surfaceColor = MaterialTheme.colorScheme.surface
    val labelStyle = MaterialTheme.typography.labelMedium.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    val centimeterUnit = stringResource(R.string.wellness_centimeter_suffix)
    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(surfaceColor)
                val rulerTop = 96.dp.toPx()
                val majorTickLength = 88.dp.toPx()
                val halfTickLength = 60.dp.toPx()
                val minorTickLength = 36.dp.toPx()
                val labelStart = majorTickLength + 10.dp.toPx()
                var millimeter = 0
                var y = rulerTop
                while (y <= size.height) {
                    val isCentimeter = millimeter % 10 == 0
                    val tickLength = when {
                        isCentimeter -> majorTickLength
                        millimeter % 5 == 0 -> halfTickLength
                        else -> minorTickLength
                    }
                    drawLine(
                        color = if (isCentimeter) rulerColor else minorTickColor,
                        start = Offset(0f, y),
                        end = Offset(tickLength, y),
                        strokeWidth = if (isCentimeter) 3.dp.toPx() else 1.dp.toPx(),
                        cap = StrokeCap.Round,
                    )
                    if (isCentimeter) {
                        drawText(
                            textMeasurer = textMeasurer,
                            text = "${WellnessUiFormat.number(millimeter / 10)} $centimeterUnit",
                            topLeft = Offset(labelStart, y - 10.dp.toPx()),
                            style = labelStyle,
                        )
                    }
                    millimeter++
                    y += pixelsPerMillimeter
                }
            }
            Column(
                modifier = Modifier.fillMaxSize().padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(R.string.wellness_ruler_title), style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onSurface)
                    TextButton(onClick = onClose) {
                        Icon(Icons.Filled.Close, contentDescription = stringResource(R.string.wellness_ruler_close), tint = MaterialTheme.colorScheme.onSurface)
                    }
                }
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                    modifier = Modifier.fillMaxWidth().padding(start = 80.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(stringResource(R.string.wellness_ruler_hint), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                        Text(stringResource(R.string.wellness_ruler_calibration), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Slider(value = scaleAdjustment, onValueChange = { scaleAdjustment = it }, valueRange = 0.9f..1.1f)
                        Button(onClick = onAddMeasurement, modifier = Modifier.fillMaxWidth()) {
                            Text(stringResource(R.string.wellness_ruler_record), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GoalCard(
    goal: WellnessGoal?,
    latest: Measurement?,
    privacyMode: Boolean,
    onSetGoal: () -> Unit,
    onClearGoal: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(stringResource(R.string.wellness_goal_title), style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
                TextButton(onClick = onSetGoal) {
                    Text(stringResource(if (goal == null) R.string.wellness_goal_set else R.string.wellness_goal_change), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                }
            }
            if (goal == null) {
                Text(stringResource(R.string.wellness_goal_empty), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                val metric = goal.metric.toGoalMetric()
                val current = latest?.valueFor(metric) ?: goal.baselineCm
                val progress = WellnessRules.goalProgress(goal.baselineCm, current, goal.targetCm)
                MetricText(metric, style = MaterialTheme.typography.labelLarge)
                Text(
                    text = if (privacyMode) stringResource(R.string.wellness_goal_private_value) else stringResource(R.string.wellness_goal_value, WellnessUiFormat.number(current), WellnessUiFormat.number(goal.targetCm)),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth().clip(MaterialTheme.shapes.extraLarge), strokeCap = StrokeCap.Round)
                Text(
                    text = stringResource(R.string.wellness_goal_remaining, WellnessUiFormat.number(kotlin.math.abs(goal.targetCm - current))),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                TextButton(onClick = onClearGoal) {
                    Icon(Icons.Filled.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                    Text(stringResource(R.string.wellness_goal_clear), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(start = 6.dp))
                }
            }
        }
    }
}

@Composable
private fun MeasurementChart(
    measurements: List<Measurement>,
    metric: GoalMetric,
    privacyMode: Boolean,
) {
    val values = measurements.map { it.valueFor(metric) }
    val chartColor = MaterialTheme.colorScheme.primary
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier.fillMaxWidth(),
    ) {
        if (values.size < 2) {
            Text(
                text = stringResource(R.string.wellness_chart_empty),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(20.dp),
            )
        } else {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = if (privacyMode) stringResource(R.string.wellness_chart_private) else stringResource(R.string.wellness_chart_latest, WellnessUiFormat.number(values.last())),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Canvas(modifier = Modifier.fillMaxWidth().height(180.dp)) {
                    val low = values.minOrNull() ?: 0.0
                    val high = values.maxOrNull() ?: low
                    val range = (high - low).takeIf { it > 0.0 } ?: 1.0
                    val horizontalStep = size.width / (values.size - 1)
                    val points = values.mapIndexed { index, value ->
                        Offset(
                            x = index * horizontalStep,
                            y = size.height - (((value - low) / range).toFloat() * (size.height * 0.8f) + size.height * 0.1f),
                        )
                    }
                    points.zipWithNext().forEach { (start, end) ->
                        drawLine(chartColor, start, end, strokeWidth = 5f, cap = StrokeCap.Round)
                    }
                    points.forEach { point -> drawCircle(chartColor, radius = 7f, center = point) }
                }
            }
        }
    }
}

@Composable
private fun MeasurementSheet(
    hasValidationError: Boolean,
    privacyMode: Boolean,
    onDismiss: () -> Unit,
    onSave: (String, String, MeasurementState) -> Boolean,
) {
    var length by rememberSaveable { mutableStateOf("") }
    var circumference by rememberSaveable { mutableStateOf("") }
    var state by rememberSaveable { mutableStateOf(MeasurementState.RELAXED) }
    var showValues by rememberSaveable { mutableStateOf(false) }
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp).padding(bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(stringResource(R.string.wellness_measurement_sheet_title), style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface)
            MeasurementState.entries.forEach { choice ->
                FilterChip(selected = state == choice, onClick = { state = choice }, label = { Text(choice.label(), style = MaterialTheme.typography.labelLarge) })
            }
            MeasurementField(length, { length = it }, stringResource(R.string.wellness_length_label), privacyMode && !showValues, { showValues = !showValues })
            MeasurementField(circumference, { circumference = it }, stringResource(R.string.wellness_circumference_label), privacyMode && !showValues, { showValues = !showValues })
            if (hasValidationError) Text(stringResource(R.string.wellness_measurement_error, WellnessUiFormat.number(1), WellnessUiFormat.number(40), WellnessUiFormat.number(1), WellnessUiFormat.number(25)), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
            Button(onClick = { if (onSave(length, circumference, state)) onDismiss() }, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.wellness_measurement_save), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Composable
private fun GoalSheet(
    initialMetric: GoalMetric,
    initialTarget: String,
    hasValidationError: Boolean,
    hasMeasurements: Boolean,
    onDismiss: () -> Unit,
    onSave: (GoalMetric, String) -> Unit,
) {
    var metric by rememberSaveable { mutableStateOf(initialMetric) }
    var target by rememberSaveable { mutableStateOf(initialTarget) }
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp).padding(bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(stringResource(R.string.wellness_goal_sheet_title), style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface)
            GoalMetric.entries.forEach { choice -> FilterChip(selected = metric == choice, onClick = { metric = choice }, label = { MetricText(choice) }) }
            OutlinedTextField(value = target, onValueChange = { target = it.filter { char -> char.isDigit() || char == '.' || char == ',' }.take(5) }, label = { Text(stringResource(R.string.wellness_goal_target_label), style = MaterialTheme.typography.labelLarge) }, suffix = { Text(stringResource(R.string.wellness_centimeter_suffix), style = MaterialTheme.typography.bodyLarge) }, keyboardOptions = KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal), singleLine = true, modifier = Modifier.fillMaxWidth())
            if (!hasMeasurements) Text(stringResource(R.string.wellness_goal_requires_measurement), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
            if (hasValidationError) Text(stringResource(R.string.wellness_goal_error), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
            Button(onClick = { onSave(metric, target) }, enabled = hasMeasurements, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.wellness_goal_save), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onPrimary) }
        }
    }
}

@Composable
private fun MeasurementField(value: String, onValueChange: (String) -> Unit, label: String, masked: Boolean, onToggleMask: () -> Unit) {
    OutlinedTextField(value = value, onValueChange = { onValueChange(it.filter { char -> char.isDigit() || char == '.' || char == ',' }.take(5)) }, label = { Text(label, style = MaterialTheme.typography.labelLarge) }, suffix = { Text(stringResource(R.string.wellness_centimeter_suffix), style = MaterialTheme.typography.bodyLarge) }, trailingIcon = { TextButton(onClick = onToggleMask) { Text(stringResource(if (masked) R.string.wellness_show_value else R.string.wellness_hide_value), style = MaterialTheme.typography.labelLarge) } }, visualTransformation = if (masked) PasswordVisualTransformation(mask = '•') else VisualTransformation.None, keyboardOptions = KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal), singleLine = true, modifier = Modifier.fillMaxWidth())
}

@Composable
private fun MetricText(metric: GoalMetric, style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.labelLarge) {
    Text(stringResource(if (metric == GoalMetric.LENGTH) R.string.wellness_length_label else R.string.wellness_circumference_label), style = style, color = MaterialTheme.colorScheme.onSurfaceVariant)
}

private fun String.toGoalMetric(): GoalMetric = runCatching { GoalMetric.valueOf(this) }.getOrDefault(GoalMetric.LENGTH)

private fun Measurement.valueFor(metric: GoalMetric): Double = if (metric == GoalMetric.LENGTH) lengthCm else circumferenceCm
