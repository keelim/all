package com.keelim.nandadiagnosis.wellness.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.keelim.common.extensions.toUiNumber
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.wellness.domain.CheckInRules
import com.keelim.nandadiagnosis.wellness.domain.DailyCheckIn
import com.keelim.nandadiagnosis.wellness.domain.MorningCondition

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DailyCheckInSheet(
    initial: DailyCheckIn?,
    onDismiss: () -> Unit,
    onSave: (DailyCheckIn) -> Unit,
    isSaving: Boolean = false,
    saveFailed: Boolean = false,
) {
    var details by rememberSaveable { mutableStateOf(false) }
    var sleep by rememberSaveable { mutableStateOf(initial?.sleep) }
    var stress by rememberSaveable { mutableStateOf(initial?.stress) }
    var energy by rememberSaveable { mutableStateOf(initial?.energy) }
    var desire by rememberSaveable { mutableStateOf(initial?.desire) }
    var confidence by rememberSaveable { mutableStateOf(initial?.confidence) }
    var morning by rememberSaveable { mutableStateOf(initial?.morningCondition) }
    var drankAlcohol by rememberSaveable { mutableStateOf(initial?.drankAlcohol) }
    var didCardio by rememberSaveable { mutableStateOf(initial?.didCardio) }
    var hasDiscomfort by rememberSaveable { mutableStateOf(initial?.hasDiscomfort) }
    var note by rememberSaveable { mutableStateOf(initial?.note.orEmpty()) }
    val draft = DailyCheckIn(
        localDate = initial?.localDate.orEmpty(),
        sleep = sleep, stress = stress, energy = energy, desire = desire, confidence = confidence,
        morningCondition = morning, drankAlcohol = drankAlcohol, didCardio = didCardio,
        hasDiscomfort = hasDiscomfort, note = note.trim(),
    )
    ModalBottomSheet(onDismissRequest = { if (!isSaving) onDismiss() }) {
        Column(
            modifier = Modifier.fillMaxWidth().navigationBarsPadding().imePadding()
                .padding(KuiTheme.spacing.space4),
            verticalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space3),
        ) {
            Label(stringResource(R.string.morning_title))
            LazyColumn(
                modifier = Modifier.weight(1f, fill = false),
                verticalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space4),
            ) {
                item {
                    ConditionPicker(stringResource(R.string.morning_energy), energy, !isSaving) { energy = it }
                    Body(stringResource(R.string.morning_energy_hint))
                }
                item {
                    ConditionPicker(stringResource(R.string.morning_sleep), sleep, !isSaving) { sleep = it }
                }
                item {
                    Label(stringResource(R.string.morning_erection))
                    Row(
                        Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space2),
                    ) {
                        MorningCondition.entries.forEach { option ->
                            FilterChip(
                                selected = morning == option,
                                enabled = !isSaving,
                                onClick = { morning = if (morning == option) null else option },
                                label = { Body(morningLabel(option)) },
                            )
                        }
                    }
                    Body(stringResource(R.string.morning_selection_hint))
                    Body(stringResource(R.string.morning_medical_note))
                }
                item {
                    OutlinedTextField(
                        value = note, onValueChange = { note = it }, enabled = !isSaving,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Body(stringResource(R.string.wellness_note_optional)) },
                        textStyle = KuiTheme.typography.bodyLarge.copy(color = KuiTheme.colorScheme.onSurface),
                        minLines = 2,
                    )
                }
                item {
                    TextButton(onClick = { details = !details }, enabled = !isSaving) {
                        Label(stringResource(if (details) R.string.morning_details_hide else R.string.morning_details))
                    }
                    AnimatedVisibility(visible = details) {
                        Column(verticalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space4)) {
                            ConditionPicker(stringResource(R.string.wellness_condition_stress), stress, !isSaving) { stress = it }
                            ConditionPicker(stringResource(R.string.wellness_condition_desire), desire, !isSaving) { desire = it }
                            ConditionPicker(stringResource(R.string.wellness_condition_confidence), confidence, !isSaving) { confidence = it }
                            BooleanPicker(stringResource(R.string.wellness_alcohol_record), drankAlcohol, !isSaving) { drankAlcohol = it }
                            BooleanPicker(stringResource(R.string.wellness_cardio_record), didCardio, !isSaving) { didCardio = it }
                            BooleanPicker(stringResource(R.string.wellness_discomfort_record), hasDiscomfort, !isSaving) { hasDiscomfort = it }
                        }
                    }
                }
            }
            if (saveFailed) Body(stringResource(R.string.morning_write_failed))
            Button(
                onClick = { onSave(draft) },
                enabled = !isSaving && CheckInRules.validate(draft).isEmpty(),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    stringResource(if (isSaving) R.string.morning_saving else R.string.wellness_checkin_save),
                    style = KuiTheme.typography.labelLarge, color = KuiTheme.colorScheme.onPrimary,
                )
            }
            TextButton(onClick = onDismiss, enabled = !isSaving) {
                Body(stringResource(R.string.wellness_later))
            }
        }
    }
}

@Composable
private fun ConditionPicker(label: String, value: Int?, enabled: Boolean, onValueChange: (Int?) -> Unit) {
    Column {
        Label(label)
        Row(
            Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space2),
        ) {
            (1..5).forEach { option ->
                FilterChip(
                    selected = value == option, enabled = enabled,
                    onClick = { onValueChange(if (value == option) null else option) },
                    label = { Body(option.toUiNumber()) },
                )
            }
        }
        Body(stringResource(R.string.morning_scale_hint))
        Body(stringResource(R.string.morning_selection_hint))
    }
}

@Composable
private fun BooleanPicker(label: String, value: Boolean?, enabled: Boolean, onValueChange: (Boolean?) -> Unit) {
    Column {
        Label(label)
        Row(horizontalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space2)) {
            listOf(true, false).forEach { option ->
                FilterChip(
                    selected = value == option, enabled = enabled,
                    onClick = { onValueChange(if (value == option) null else option) },
                    label = { Body(stringResource(if (option) R.string.wellness_yes else R.string.wellness_no)) },
                )
            }
        }
    }
}

@Composable
internal fun morningLabel(value: MorningCondition?): String = stringResource(
    when (value) {
        MorningCondition.YES -> R.string.morning_yes
        MorningCondition.NO -> R.string.morning_no
        MorningCondition.NOT_CHECKED -> R.string.morning_unknown
        null -> R.string.morning_unanswered
    },
)

@Composable
private fun Label(text: String) {
    Text(text, style = KuiTheme.typography.titleMedium, color = KuiTheme.colorScheme.onSurface)
}

@Composable
private fun Body(text: String) {
    Text(text, style = KuiTheme.typography.bodyMedium, color = KuiTheme.colorScheme.onSurfaceVariant)
}
