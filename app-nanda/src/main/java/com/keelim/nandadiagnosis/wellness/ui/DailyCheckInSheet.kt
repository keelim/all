package com.keelim.nandadiagnosis.wellness.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.wellness.domain.DailyCheckIn
import com.keelim.nandadiagnosis.wellness.domain.MorningCondition
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DailyCheckInSheet(
    initial: DailyCheckIn?,
    onDismiss: () -> Unit,
    onSave: (DailyCheckIn) -> Unit,
) {
    var step by rememberSaveable { mutableIntStateOf(1) }
    var sleep by rememberSaveable { mutableIntStateOf(initial?.sleep ?: 3) }
    var stress by rememberSaveable { mutableIntStateOf(initial?.stress ?: 3) }
    var energy by rememberSaveable { mutableIntStateOf(initial?.energy ?: 3) }
    var desire by rememberSaveable { mutableIntStateOf(initial?.desire ?: 3) }
    var confidence by rememberSaveable { mutableIntStateOf(initial?.confidence ?: 3) }
    var morning by rememberSaveable {
        mutableStateOf(initial?.morningCondition ?: MorningCondition.NOT_CHECKED)
    }
    var drankAlcohol by rememberSaveable { mutableStateOf(initial?.drankAlcohol ?: false) }
    var didCardio by rememberSaveable { mutableStateOf(initial?.didCardio ?: false) }
    var hasDiscomfort by rememberSaveable { mutableStateOf(initial?.hasDiscomfort ?: false) }
    var note by rememberSaveable { mutableStateOf(initial?.note.orEmpty()) }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Text(
                text = stringResource(
                    if (step == 1) R.string.wellness_checkin_step_one else
                        R.string.wellness_checkin_step_two,
                ),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            LazyColumn(
                modifier = Modifier.weight(1f, fill = false),
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                if (step == 1) {
                    item {
                        ConditionPicker(
                            label = stringResource(R.string.wellness_condition_sleep),
                            low = stringResource(R.string.wellness_condition_sleep_low),
                            high = stringResource(R.string.wellness_condition_sleep_high),
                            value = sleep,
                            onValueChange = { sleep = it },
                        )
                    }
                    item {
                        ConditionPicker(
                            label = stringResource(R.string.wellness_condition_stress),
                            low = stringResource(R.string.wellness_condition_low),
                            high = stringResource(R.string.wellness_condition_high),
                            value = stress,
                            onValueChange = { stress = it },
                        )
                    }
                    item {
                        ConditionPicker(
                            label = stringResource(R.string.wellness_condition_energy),
                            low = stringResource(R.string.wellness_condition_low),
                            high = stringResource(R.string.wellness_condition_high),
                            value = energy,
                            onValueChange = { energy = it },
                        )
                    }
                    item {
                        ConditionPicker(
                            label = stringResource(R.string.wellness_condition_desire),
                            low = stringResource(R.string.wellness_condition_low),
                            high = stringResource(R.string.wellness_condition_high),
                            value = desire,
                            onValueChange = { desire = it },
                        )
                    }
                    item {
                        ConditionPicker(
                            label = stringResource(R.string.wellness_condition_confidence),
                            low = stringResource(R.string.wellness_condition_low),
                            high = stringResource(R.string.wellness_condition_high),
                            value = confidence,
                            onValueChange = { confidence = it },
                        )
                    }
                } else {
                    item {
                        Text(
                            text = stringResource(R.string.wellness_morning_condition),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            MorningCondition.entries.forEach { option ->
                                FilterChip(
                                    selected = morning == option,
                                    onClick = { morning = option },
                                    label = {
                                        Text(
                                            text = stringResource(
                                                when (option) {
                                                    MorningCondition.YES -> R.string.wellness_yes
                                                    MorningCondition.NO -> R.string.wellness_no
                                                    MorningCondition.NOT_CHECKED ->
                                                        R.string.wellness_not_checked
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
                    item {
                        OptionalSwitch(
                            label = stringResource(R.string.wellness_alcohol_record),
                            checked = drankAlcohol,
                            onCheckedChange = { drankAlcohol = it },
                        )
                    }
                    item {
                        OptionalSwitch(
                            label = stringResource(R.string.wellness_cardio_record),
                            checked = didCardio,
                            onCheckedChange = { didCardio = it },
                        )
                    }
                    item {
                        OptionalSwitch(
                            label = stringResource(R.string.wellness_discomfort_record),
                            checked = hasDiscomfort,
                            onCheckedChange = { hasDiscomfort = it },
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = note,
                            onValueChange = { note = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = {
                                Text(
                                    text = stringResource(R.string.wellness_note_optional),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            },
                            textStyle = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                            ),
                            minLines = 2,
                        )
                    }
                }
            }
            Button(
                onClick = {
                    if (step == 1) {
                        step = 2
                    } else {
                        onSave(
                            DailyCheckIn(
                                localDate = LocalDate.now().toString(),
                                sleep = sleep,
                                stress = stress,
                                energy = energy,
                                desire = desire,
                                confidence = confidence,
                                morningCondition = morning,
                                drankAlcohol = drankAlcohol,
                                didCardio = didCardio,
                                hasDiscomfort = hasDiscomfort,
                                note = note.trim(),
                            ),
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
            ) {
                Text(
                    text = stringResource(
                        if (step == 1) R.string.wellness_next else R.string.wellness_checkin_save,
                    ),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
            TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.wellness_later),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
private fun ConditionPicker(
    label: String,
    low: String,
    high: String,
    value: Int,
    onValueChange: (Int) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            (1..5).forEach { option ->
                FilterChip(
                    selected = value == option,
                    onClick = { onValueChange(option) },
                    label = {
                        Text(
                            text = option.toString(),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    },
                )
            }
        }
        Text(
            text = stringResource(R.string.wellness_condition_scale, low, high),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun OptionalSwitch(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
