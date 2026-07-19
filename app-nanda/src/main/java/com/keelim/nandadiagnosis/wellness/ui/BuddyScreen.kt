package com.keelim.nandadiagnosis.wellness.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.keelim.nandadiagnosis.R
import com.keelim.model.wellness.Measurement
import com.keelim.nandadiagnosis.wellness.domain.MeasurementState
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
internal fun BuddyScreen(
    measurements: List<Measurement>,
    hasValidationError: Boolean,
    privacyMode: Boolean,
    onSaveMeasurement: (String, String, MeasurementState) -> Unit,
) {
    var length by rememberSaveable { mutableStateOf("") }
    var circumference by rememberSaveable { mutableStateOf("") }
    var measurementState by rememberSaveable { mutableStateOf(MeasurementState.RELAXED) }
    var showInputValues by rememberSaveable { mutableStateOf(false) }
    var showHistory by rememberSaveable { mutableStateOf(false) }
    val today = LocalDate.now()
    val recentDates = (6L downTo 0L).map(today::minusDays)
    val recordedDates = measurements.mapTo(mutableSetOf()) { it.localDate }

    LazyColumn(
        modifier = Modifier.fillMaxSize().imePadding(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        item(key = "buddy_heading") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
        item(key = "measurement_state_choices") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(R.string.wellness_measurement_state_label),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    MeasurementState.entries.forEach { state ->
                        StateChip(
                            selected = measurementState == state,
                            label = state.label(),
                            onClick = { measurementState = state },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }
        item(key = "length_input") {
            MeasurementField(
                value = length,
                onValueChange = { length = it },
                label = stringResource(R.string.wellness_length_label),
                masked = privacyMode && !showInputValues,
                onToggleMask = { showInputValues = !showInputValues },
            )
        }
        item(key = "circumference_input") {
            MeasurementField(
                value = circumference,
                onValueChange = { circumference = it },
                label = stringResource(R.string.wellness_circumference_label),
                masked = privacyMode && !showInputValues,
                onToggleMask = { showInputValues = !showInputValues },
            )
        }
        item(key = "measurement_error") {
            AnimatedVisibility(visible = hasValidationError) {
                Text(
                    text =
                        stringResource(
                            R.string.wellness_measurement_error,
                            WellnessUiFormat.number(1),
                            WellnessUiFormat.number(40),
                            WellnessUiFormat.number(1),
                            WellnessUiFormat.number(25),
                        ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
        item(key = "measurement_save") {
            Button(
                onClick = { onSaveMeasurement(length, circumference, measurementState) },
                modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                shape = RoundedCornerShape(14.dp),
            ) {
                Text(
                    text = stringResource(R.string.wellness_measurement_save),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
        item(key = "recent_summary") {
            RecentMeasurementSummary(
                dates = recentDates,
                recordedDates = recordedDates,
                onShowDetails = { showHistory = !showHistory },
                detailsVisible = showHistory,
            )
        }
        item(key = "measurement_history") {
            AnimatedVisibility(visible = showHistory) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = stringResource(R.string.wellness_history_title),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    if (measurements.isEmpty()) {
                        EmptyListText()
                    }
                }
            }
        }
        if (showHistory) {
            items(
                items = measurements,
                key = { measurement -> measurement.localDate },
            ) { measurement ->
                MeasurementCard(
                    measurement = measurement,
                    privacyMode = privacyMode,
                    modifier = Modifier.animateItem(),
                )
            }
        }
    }
}

@Composable
private fun MeasurementField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    masked: Boolean,
    onToggleMask: () -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = { input ->
            onValueChange(input.filter { it.isDigit() || it == '.' || it == ',' }.take(5))
        },
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        suffix = {
            Text(
                text = stringResource(R.string.wellness_centimeter_suffix),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        trailingIcon = {
            TextButton(onClick = onToggleMask) {
                Text(
                    text =
                        stringResource(
                            if (masked) R.string.wellness_show_value else R.string.wellness_hide_value,
                        ),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        },
        visualTransformation =
            if (masked) PasswordVisualTransformation(mask = '•') else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().heightIn(min = 88.dp),
    )
}

@Composable
internal fun StateChip(
    selected: Boolean,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color =
                    if (selected) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
            )
        },
        leadingIcon =
            if (selected) {
                {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(18.dp),
                    )
                }
            } else {
                null
            },
        colors =
            FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary,
            ),
        modifier = modifier.heightIn(min = 48.dp),
    )
}

@Composable
private fun RecentMeasurementSummary(
    dates: List<LocalDate>,
    recordedDates: Set<String>,
    onShowDetails: () -> Unit,
    detailsVisible: Boolean,
) {
    val recordedCount = dates.count { it.toString() in recordedDates }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder(),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.wellness_recent_seven_days),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text =
                        stringResource(
                            R.string.wellness_days_recorded,
                            WellnessUiFormat.number(recordedCount),
                        ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                dates.forEach { date ->
                    val recorded = date.toString() in recordedDates
                    val isToday = date == dates.last()
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = weekdayLabel(date.dayOfWeek),
                            style = MaterialTheme.typography.labelMedium,
                            color =
                                if (isToday) {
                                    MaterialTheme.colorScheme.secondary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                },
                        )
                        Surface(
                            shape = CircleShape,
                            color =
                                when {
                                    isToday && recorded -> MaterialTheme.colorScheme.secondary
                                    recorded -> MaterialTheme.colorScheme.primary
                                    else -> MaterialTheme.colorScheme.surfaceVariant
                                },
                            border =
                                if (recorded) {
                                    null
                                } else {
                                    CardDefaults.outlinedCardBorder()
                                },
                            modifier = Modifier.size(38.dp).clip(CircleShape),
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (recorded) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null,
                                        tint =
                                            if (isToday) {
                                                MaterialTheme.colorScheme.onSecondary
                                            } else {
                                                MaterialTheme.colorScheme.onPrimary
                                            },
                                        modifier = Modifier.size(20.dp),
                                    )
                                }
                            }
                        }
                    }
                }
            }
            TextButton(
                onClick = onShowDetails,
                modifier = Modifier.align(Alignment.End).heightIn(min = 48.dp),
            ) {
                Text(
                    text =
                        stringResource(
                            if (detailsVisible) {
                                R.string.wellness_hide_value
                            } else {
                                R.string.wellness_record_details
                            },
                        ),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
private fun weekdayLabel(dayOfWeek: DayOfWeek): String =
    stringResource(
        when (dayOfWeek) {
            DayOfWeek.MONDAY -> R.string.wellness_weekday_monday
            DayOfWeek.TUESDAY -> R.string.wellness_weekday_tuesday
            DayOfWeek.WEDNESDAY -> R.string.wellness_weekday_wednesday
            DayOfWeek.THURSDAY -> R.string.wellness_weekday_thursday
            DayOfWeek.FRIDAY -> R.string.wellness_weekday_friday
            DayOfWeek.SATURDAY -> R.string.wellness_weekday_saturday
            DayOfWeek.SUNDAY -> R.string.wellness_weekday_sunday
        },
    )

@Composable
private fun MeasurementCard(
    measurement: Measurement,
    privacyMode: Boolean,
    modifier: Modifier = Modifier,
) {
    var revealed by rememberSaveable(measurement.localDate) { mutableStateOf(false) }
    val hideValues = privacyMode && !revealed

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder(),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = WellnessUiFormat.date(measurement.localDate),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                TextButton(onClick = { revealed = !revealed }) {
                    Text(
                        text =
                            stringResource(
                                if (hideValues) R.string.wellness_show_value else R.string.wellness_hide_value,
                            ),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
            Text(
                text =
                    stringResource(
                        R.string.wellness_measurement_state_value,
                        measurementStateLabel(measurement.state),
                    ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text =
                    if (hideValues) {
                        stringResource(R.string.wellness_measurement_length_value, "•••")
                    } else {
                        stringResource(
                            R.string.wellness_measurement_length_value,
                            WellnessUiFormat.number(measurement.lengthCm),
                        )
                    },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text =
                    if (hideValues) {
                        stringResource(R.string.wellness_measurement_circumference_value, "•••")
                    } else {
                        stringResource(
                            R.string.wellness_measurement_circumference_value,
                            WellnessUiFormat.number(measurement.circumferenceCm),
                        )
                    },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun EmptyListText(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.wellness_measurement_empty),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier,
    )
}
