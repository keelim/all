package com.keelim.nandadiagnosis.wellness.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.keelim.model.wellness.DailyTimeBudget
import com.keelim.model.wellness.RecoveryGoalType
import com.keelim.model.wellness.Routine
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.wellness.RecoveryRoutineDraft
import com.keelim.nandadiagnosis.wellness.WellnessUiState
import com.keelim.nandadiagnosis.wellness.WellnessValidationError
import com.keelim.nandadiagnosis.wellness.domain.RoutineKind
import java.time.LocalDate

@Composable
internal fun PlanScreen(
    uiState: WellnessUiState,
    onAddRoutine: (String, RoutineKind) -> Boolean,
    onSaveRecoveryGoal: (
        RecoveryGoalType,
        DailyTimeBudget,
        List<RecoveryRoutineDraft>,
    ) -> Unit,
    onSetRoutineCompletion: (Routine, Boolean, Int?) -> Unit,
    onDeleteRoutine: (Routine) -> Unit,
) {
    val today = remember { LocalDate.now() }
    val todayIso = today.toString()
    val weekStart = today.minusDays(today.dayOfWeek.value.toLong() - 1)
    val weekCompletions = uiState.completions.count {
        runCatching { LocalDate.parse(it.localDate) }.getOrNull() in weekStart..today
    }
    val weeklyTarget = (uiState.routines.size * 3).coerceAtLeast(1)
    val weeklyProgress by animateFloatAsState(
        targetValue = (weekCompletions.toFloat() / weeklyTarget).coerceIn(0f, 1f),
        animationSpec = tween(500),
        label = "planWeeklyProgress",
    )
    var showAddSheet by rememberSaveable { mutableStateOf(false) }
    var showGoalSheet by rememberSaveable { mutableStateOf(false) }
    var startWithRecommendations by rememberSaveable { mutableStateOf(false) }
    var pendingDelete by remember { mutableStateOf<Routine?>(null) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddSheet = true }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.wellness_plan_add),
                )
            }
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            item(key = "recoveryGoal") {
                RecoveryGoalCard(
                    goal = uiState.recoveryGoal,
                    weeklyActionCompletions = uiState.weeklyActionCompletions,
                    weeklyActiveDays = uiState.weeklyActiveDays,
                    onChooseGoal = {
                        startWithRecommendations = false
                        showGoalSheet = true
                    },
                    onViewRecommendations = {
                        startWithRecommendations = true
                        showGoalSheet = true
                    },
                    onChangeGoal = {
                        startWithRecommendations = false
                        showGoalSheet = true
                    },
                    modifier = Modifier.animateItem(),
                )
            }
            item(key = "planHeader") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.wellness_plan_title),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Text(
                        text = stringResource(R.string.wellness_plan_intro),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = stringResource(
                            R.string.wellness_plan_week_range,
                            WellnessUiFormat.date(weekStart),
                            WellnessUiFormat.date(weekStart.plusDays(6)),
                        ),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            item(key = "planProgress") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(
                            R.string.wellness_plan_weekly_progress,
                            weekCompletions,
                            weeklyTarget,
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    LinearProgressIndicator(
                        progress = { weeklyProgress },
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
            }
            if (uiState.routines.isEmpty()) {
                item(key = "planEmpty") {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                        ),
                        shape = RoundedCornerShape(20.dp),
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Text(
                                text = stringResource(R.string.wellness_plan_empty),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Button(onClick = { showAddSheet = true }) {
                                Text(
                                    text = stringResource(R.string.wellness_plan_first_action),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                )
                            }
                        }
                    }
                }
            } else {
                items(uiState.routines, key = { it.id }) { routine ->
                    val completedThisWeek = uiState.completions.count { completion ->
                        completion.routineId == routine.id &&
                            runCatching { LocalDate.parse(completion.localDate) }
                                .getOrNull() in weekStart..today
                    }
                    val completedToday = uiState.completions.any {
                        it.routineId == routine.id && it.localDate == todayIso
                    }
                    PlanCard(
                        routine = routine,
                        completedThisWeek = completedThisWeek,
                        completedToday = completedToday,
                        onToggle = {
                            onSetRoutineCompletion(routine, !completedToday, null)
                        },
                        onDelete = { pendingDelete = routine },
                        modifier = Modifier.animateItem(),
                    )
                }
            }
        }
    }

    if (showAddSheet) {
        AddPlanSheet(
            hasNameError = WellnessValidationError.ROUTINE_NAME in uiState.validationErrors,
            onDismiss = { showAddSheet = false },
            onAdd = { name, kind ->
                if (onAddRoutine(name, kind)) showAddSheet = false
            },
        )
    }

    if (showGoalSheet) {
        RecoveryGoalSheet(
            activeGoal = uiState.recoveryGoal,
            startWithRecommendations = startWithRecommendations,
            onDismiss = { showGoalSheet = false },
            onSave = { type, timeBudget, selectedActions ->
                onSaveRecoveryGoal(type, timeBudget, selectedActions)
                showGoalSheet = false
            },
        )
    }

    pendingDelete?.let { routine ->
        AlertDialog(
            onDismissRequest = { pendingDelete = null },
            title = {
                Text(
                    text = stringResource(R.string.wellness_routine_delete_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            },
            text = {
                Text(
                    text = stringResource(
                        R.string.wellness_routine_delete_message,
                        routine.name,
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteRoutine(routine)
                    pendingDelete = null
                }) {
                    Text(
                        text = stringResource(R.string.wellness_confirm_delete),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingDelete = null }) {
                    Text(
                        text = stringResource(R.string.wellness_cancel),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            },
        )
    }
}

@Composable
private fun PlanCard(
    routine: Routine,
    completedThisWeek: Int,
    completedToday: Boolean,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardColor by animateColorAsState(
        targetValue = if (completedToday) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(260),
        label = "planCardColor",
    )
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = routine.name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = routineKindLabel(routine.kind),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = stringResource(
                    R.string.wellness_plan_item_progress,
                    completedThisWeek,
                    3,
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedButton(onClick = onToggle, modifier = Modifier.weight(1f)) {
                    AnimatedContent(
                        targetState = completedToday,
                        transitionSpec = {
                            fadeIn(tween(180)) togetherWith fadeOut(tween(100))
                        },
                        label = "planActionButton",
                    ) { completed ->
                        Text(
                            text = stringResource(
                                if (completed) {
                                    R.string.wellness_action_completed
                                } else {
                                    R.string.wellness_action_complete
                                },
                            ),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
                TextButton(onClick = onDelete) {
                    Text(
                        text = stringResource(R.string.wellness_routine_delete),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddPlanSheet(
    hasNameError: Boolean,
    onDismiss: () -> Unit,
    onAdd: (String, RoutineKind) -> Unit,
) {
    var name by rememberSaveable { mutableStateOf("") }
    var kind by rememberSaveable { mutableStateOf(RoutineKind.RUNNING) }

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
                text = stringResource(R.string.wellness_plan_add),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                RoutineKind.entries.forEach { option ->
                    FilterChip(
                        selected = kind == option,
                        onClick = { kind = option },
                        label = {
                            Text(
                                text = routineKindLabel(option.name),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        },
                    )
                }
            }
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(R.string.wellness_plan_action_name),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                isError = hasNameError,
                supportingText = if (hasNameError) {
                    {
                        Text(
                            text = stringResource(R.string.wellness_routine_name_error),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                } else {
                    null
                },
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                ),
            )
            Button(
                onClick = { onAdd(name, kind) },
                modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
            ) {
                Text(
                    text = stringResource(R.string.wellness_plan_add_action),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}
