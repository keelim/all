package com.keelim.nandadiagnosis.wellness.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.keelim.model.wellness.Routine
import com.keelim.model.wellness.RoutineCompletion
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.wellness.domain.RoutineKind
import com.keelim.nandadiagnosis.wellness.domain.WellnessRules
import java.time.LocalDate

@Composable
internal fun RoutineScreen(
    routines: List<Routine>,
    completions: List<RoutineCompletion>,
    hasNameError: Boolean,
    hasDurationError: Boolean,
    onAddRoutine: (String, RoutineKind) -> Boolean,
    onSetRoutineCompletion: (Routine, Boolean, Int?) -> Unit,
    onDeleteRoutine: (Routine) -> Unit,
) {
    var showAddSheet by rememberSaveable { mutableStateOf(false) }
    var selectedKind by rememberSaveable { mutableStateOf(RoutineKind.SUPPLEMENT) }
    var routinePendingDelete by remember { mutableStateOf<Routine?>(null) }
    val today = LocalDate.now()
    val todayIso = today.toString()
    val selectedRoutines = remember(routines, selectedKind) {
        routines.filter { it.kind == selectedKind.name }
    }

    Row(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.width(84.dp).padding(top = 20.dp)) {
            RoutineKind.entries.forEach { kind ->
                val selected = selectedKind == kind
                Surface(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .heightIn(min = 54.dp)
                            .selectable(
                                selected = selected,
                                onClick = { selectedKind = kind },
                                role = Role.Tab,
                            ),
                    shape = if (selected) RoundedCornerShape(topEnd = 18.dp, bottomEnd = 18.dp) else RectangleShape,
                    color = if (selected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainer,
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = kind.label(),
                            style = MaterialTheme.typography.labelLarge,
                            color = if (selected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize().weight(1f),
            contentPadding = PaddingValues(start = 12.dp, top = 20.dp, end = 20.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item(key = "routine_add") {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = { showAddSheet = true }) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                        Text(stringResource(R.string.wellness_routine_add), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(start = 4.dp))
                    }
                }
            }
            item(key = "routine_duration_error") {
                AnimatedVisibility(visible = hasDurationError) {
                    Text(stringResource(R.string.wellness_routine_duration_error, WellnessUiFormat.number(1), WellnessUiFormat.number(1_440)), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                }
            }
            if (selectedRoutines.isEmpty()) {
                item(key = "routine_empty") {
                    Text(stringResource(R.string.wellness_routine_empty), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                items(selectedRoutines, key = { it.id }) { routine ->
                    val routineCompletions = completions.filter { it.routineId == routine.id }
                    val summary = WellnessRules.sevenDaySummary(
                        today = today,
                        createdLocalDate = runCatching { LocalDate.parse(routine.createdLocalDate) }.getOrDefault(today),
                        completedLocalDates = routineCompletions.mapNotNull { runCatching { LocalDate.parse(it.localDate) }.getOrNull() }.toSet(),
                    )
                    RoutineCard(
                        routine = routine,
                        todayCompletion = routineCompletions.firstOrNull { it.localDate == todayIso },
                        summary = summary,
                        onSetCompletion = { checked, duration -> onSetRoutineCompletion(routine, checked, duration) },
                        onDelete = { routinePendingDelete = routine },
                        modifier = Modifier.animateItem(),
                    )
                }
            }
        }
    }

    if (showAddSheet) {
        AddRoutineSheet(
            kind = selectedKind,
            hasNameError = hasNameError,
            onDismiss = { showAddSheet = false },
            onAdd = { name -> if (onAddRoutine(name, selectedKind)) showAddSheet = false },
        )
    }
    routinePendingDelete?.let { routine ->
        AlertDialog(
            onDismissRequest = { routinePendingDelete = null },
            title = { Text(stringResource(R.string.wellness_routine_delete_title), style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface) },
            text = { Text(stringResource(R.string.wellness_routine_delete_message, routine.name), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) },
            confirmButton = { TextButton(onClick = { onDeleteRoutine(routine); routinePendingDelete = null }) { Text(stringResource(R.string.wellness_confirm_delete), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.error) } },
            dismissButton = { TextButton(onClick = { routinePendingDelete = null }) { Text(stringResource(R.string.wellness_cancel), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary) } },
        )
    }
}

@Composable
private fun AddRoutineSheet(
    kind: RoutineKind,
    hasNameError: Boolean,
    onDismiss: () -> Unit,
    onAdd: (String) -> Unit,
) {
    var name by rememberSaveable { mutableStateOf("") }
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp).padding(bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(stringResource(R.string.wellness_routine_add_category_title, kind.label()), style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface)
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text(stringResource(R.string.wellness_routine_name_label), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant) }, singleLine = true, modifier = Modifier.fillMaxWidth())
            if (hasNameError) Text(stringResource(R.string.wellness_routine_name_error), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
            Button(onClick = { onAdd(name) }, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.wellness_routine_add), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onPrimary) }
        }
    }
}
