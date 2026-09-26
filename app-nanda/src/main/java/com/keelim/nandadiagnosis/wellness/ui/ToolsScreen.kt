package com.keelim.nandadiagnosis.wellness.ui

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.keelim.model.wellness.Measurement
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.wellness.WellnessUiState
import com.keelim.nandadiagnosis.wellness.domain.MeasurementState

@Composable
internal fun ToolsScreen(
    uiState: WellnessUiState,
    privacyMode: Boolean,
    onPrivacyModeChange: (Boolean) -> Unit,
    onSaveMeasurement: (String, String, MeasurementState) -> Boolean,
    privacyOptionsRequired: Boolean,
    onShowPrivacyOptions: () -> Unit,
) {
    var showMeasurements by rememberSaveable { mutableStateOf(false) }
    if (showMeasurements) {
        MeasurementToolScreen(
            measurements = uiState.measurements,
            privacyMode = privacyMode,
            hasValidationError = com.keelim.nandadiagnosis.wellness.WellnessValidationError.MEASUREMENT in
                uiState.validationErrors,
            onBack = { showMeasurements = false },
            onSaveMeasurement = onSaveMeasurement,
        )
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp),
    ) {
        item(key = "toolsHeader") {
            Text(
                text = stringResource(R.string.wellness_tools_title),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
        item(key = "healthTools") {
            ToolSection(title = stringResource(R.string.wellness_tools_health)) {
                ToolButton(
                    title = stringResource(R.string.wellness_tools_measurement),
                    onClick = { showMeasurements = true },
                )
                ToolLabel(stringResource(R.string.wellness_tools_supplements))
                ToolLabel(stringResource(R.string.wellness_tools_self_check))
                ToolLabel(stringResource(R.string.wellness_tools_care_notes))
            }
        }
        item(key = "privacyTools") {
            ToolSection(title = stringResource(R.string.wellness_tools_privacy)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(R.string.wellness_privacy_mode),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Switch(
                        checked = privacyMode,
                        onCheckedChange = onPrivacyModeChange,
                    )
                }
                if (privacyOptionsRequired) {
                    TextButton(
                        onClick = onShowPrivacyOptions,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = stringResource(R.string.wellness_privacy_options),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
                ToolLabel(stringResource(R.string.wellness_tools_app_lock))
                ToolLabel(stringResource(R.string.wellness_tools_recent_blur))
                ToolLabel(stringResource(R.string.wellness_tools_capture))
            }
        }
        item(key = "dataTools") {
            ToolSection(title = stringResource(R.string.wellness_tools_data)) {
                ToolLabel(stringResource(R.string.wellness_tools_export))
                ToolLabel(stringResource(R.string.wellness_tools_delete_all))
                ToolLabel(stringResource(R.string.wellness_tools_storage))
                ToolLabel(stringResource(R.string.wellness_tools_privacy_policy))
            }
        }
        item(key = "appInfo") {
            ToolSection(title = stringResource(R.string.wellness_tools_app_info)) {
                Text(
                    text = stringResource(R.string.wellness_medical_disclaimer),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun ToolSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SectionTitle(title)
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(20.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                content()
            }
        }
    }
}

@Composable
private fun ToolButton(
    title: String,
    onClick: () -> Unit,
) {
    TextButton(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun ToolLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
private fun MeasurementToolScreen(
    measurements: List<Measurement>,
    privacyMode: Boolean,
    hasValidationError: Boolean,
    onBack: () -> Unit,
    onSaveMeasurement: (String, String, MeasurementState) -> Boolean,
) {
    var showAddSheet by rememberSaveable { mutableStateOf(false) }
    var showRuler by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        item(key = "measurementHeader") {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.wellness_back),
                    )
                }
                Text(
                    text = stringResource(R.string.wellness_tools_measurement),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    text = stringResource(R.string.wellness_measurement_guidance),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { showAddSheet = true }) {
                        Text(
                            text = stringResource(R.string.wellness_measurement_add),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                    OutlinedButton(onClick = { showRuler = !showRuler }) {
                        Text(
                            text = stringResource(R.string.wellness_ruler_mode),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
        }
        if (showRuler) {
            item(key = "ruler") { RulerCard() }
        }
        if (measurements.isEmpty()) {
            item(key = "measurementEmpty") {
                SupportingCard(stringResource(R.string.wellness_measurement_empty))
            }
        } else {
            items(measurements.sortedByDescending { it.localDate }, key = { it.localDate }) {
                measurement ->
                MeasurementCard(measurement, privacyMode, Modifier.animateItem())
            }
        }
    }

    if (showAddSheet) {
        MeasurementSheet(
            hasValidationError = hasValidationError,
            onDismiss = { showAddSheet = false },
            onSave = { length, circumference, state ->
                if (onSaveMeasurement(length, circumference, state)) {
                    showAddSheet = false
                }
            },
        )
    }
}

@Composable
private fun RulerCard() {
    val tickColor = MaterialTheme.colorScheme.primary
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = stringResource(R.string.wellness_ruler_hint),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Canvas(modifier = Modifier.fillMaxWidth().height(220.dp)) {
                val spacing = size.height / 10f
                repeat(11) { index ->
                    val y = spacing * index
                    drawLine(
                        color = tickColor,
                        start = Offset(0f, y),
                        end = Offset(if (index % 5 == 0) size.width * .35f else size.width * .2f, y),
                        strokeWidth = 3f,
                    )
                }
            }
        }
    }
}

@Composable
private fun MeasurementCard(
    measurement: Measurement,
    privacyMode: Boolean,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = measurement.localDate,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = if (privacyMode) {
                    stringResource(R.string.wellness_hidden)
                } else {
                    stringResource(
                        R.string.wellness_measurement_pair,
                        measurement.lengthCm,
                        measurement.circumferenceCm,
                    )
                },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MeasurementSheet(
    hasValidationError: Boolean,
    onDismiss: () -> Unit,
    onSave: (String, String, MeasurementState) -> Unit,
) {
    var length by rememberSaveable { mutableStateOf("") }
    var circumference by rememberSaveable { mutableStateOf("") }
    var state by rememberSaveable { mutableStateOf(MeasurementState.RELAXED) }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = stringResource(R.string.wellness_measurement_sheet_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            OutlinedTextField(
                value = length,
                onValueChange = { length = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(R.string.wellness_length_label),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                isError = hasValidationError,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                ),
            )
            OutlinedTextField(
                value = circumference,
                onValueChange = { circumference = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(R.string.wellness_circumference_label),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                isError = hasValidationError,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                ),
            )
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                MeasurementState.entries.forEach { option ->
                    FilterChip(
                        selected = state == option,
                        onClick = { state = option },
                        label = {
                            Text(
                                text = measurementStateLabel(option),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        },
                    )
                }
            }
            if (hasValidationError) {
                Text(
                    text = stringResource(
                        R.string.wellness_measurement_error,
                        1,
                        40,
                        1,
                        25,
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
            Button(
                onClick = { onSave(length, circumference, state) },
                modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
            ) {
                Text(
                    text = stringResource(R.string.wellness_measurement_save),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}

@Composable
private fun measurementStateLabel(state: MeasurementState): String =
    stringResource(
        when (state) {
            MeasurementState.RELAXED -> R.string.wellness_measurement_state_relaxed
            MeasurementState.STRETCHED -> R.string.wellness_measurement_state_stretched
            MeasurementState.MAXIMUM -> R.string.wellness_measurement_state_maximum
        },
    )
