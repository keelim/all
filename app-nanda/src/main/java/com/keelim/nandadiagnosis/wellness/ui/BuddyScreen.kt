package com.keelim.nandadiagnosis.wellness.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.keelim.model.wellness.Routine
import com.keelim.common.extensions.toUiNumber
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.wellness.WellnessUiState
import com.keelim.nandadiagnosis.wellness.domain.DailyCheckIn
import com.keelim.nandadiagnosis.wellness.domain.InsightCalculator
import com.keelim.nandadiagnosis.wellness.domain.WellnessRules
import java.time.LocalDate
import kotlinx.coroutines.launch

@Composable
internal fun TodayScreen(
    uiState: WellnessUiState,
    privacyMode: Boolean,
    onSaveCheckIn: suspend (DailyCheckIn) -> Boolean,
    onDeleteCheckIn: suspend (String) -> Boolean = { false },
    onSetRoutineCompletion: (Routine, Boolean, Int?) -> Unit,
) {
    val today = uiState.today
    var editingDate by rememberSaveable { mutableStateOf<String?>(null) }
    var deletingDate by rememberSaveable { mutableStateOf<String?>(null) }
    var writing by remember { mutableStateOf(false) }
    var writeFailed by remember { mutableStateOf(false) }
    val todayIso = today.toString()
    val todayCheckIn = uiState.checkIns.firstOrNull { it.localDate == todayIso }
    val todayCompletions = uiState.completions.filter { it.localDate == todayIso }
    val visibleRoutines = uiState.routines.take(3)
    val summaries = uiState.routines.map { routine ->
        WellnessRules.sevenDaySummary(
            today = today,
            createdLocalDate = runCatching {
                LocalDate.parse(routine.createdLocalDate)
            }.getOrDefault(today),
            completedLocalDates = uiState.completions
                .filter { it.routineId == routine.id }
                .mapNotNull { runCatching { LocalDate.parse(it.localDate) }.getOrNull() }
                .toSet(),
        )
    }
    val completed = summaries.sumOf { it.completedDays }
    val eligible = summaries.sumOf { it.eligibleDays }
    val progress = if (eligible == 0) 0f else completed.toFloat() / eligible
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(500),
        label = "todayWeeklyProgress",
    )
    val insight = InsightCalculator.firstPattern(uiState.checkIns)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val savedMessage = stringResource(R.string.wellness_checkin_saved)
    var showCheckIn by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp),
        ) {
            if (uiState.isLoading) {
                item(key = "loading") {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
            }
            item(key = "greeting") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.wellness_today_greeting),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Text(
                        text = stringResource(R.string.wellness_today_intro),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            item(key = "checkIn") {
                CheckInCard(
                    checkIn = todayCheckIn,
                    privacyMode = privacyMode,
                    onClick = { editingDate = todayCheckIn?.localDate; writeFailed = false; showCheckIn = true },
                )
            }
            item(key = "morningHistory") {
                MorningHistory(
                    today = today, checkIns = uiState.checkIns, privacyMode = privacyMode,
                    enabled = !writing && !uiState.isCheckInWriting,
                    onEdit = { date -> editingDate = date; writeFailed = false; showCheckIn = true },
                    onDelete = { date -> deletingDate = date; writeFailed = false },
                )
            }
            item(key = "actionsTitle") {
                SectionTitle(stringResource(R.string.wellness_today_actions))
            }
            if (visibleRoutines.isEmpty()) {
                item(key = "actionsEmpty") {
                    SupportingCard(stringResource(R.string.wellness_today_actions_empty))
                }
            } else {
                items(visibleRoutines, key = { it.id }) { routine ->
                    val isComplete = todayCompletions.any { it.routineId == routine.id }
                    TodayActionCard(
                        routine = routine,
                        isComplete = isComplete,
                        onToggle = {
                            onSetRoutineCompletion(routine, !isComplete, null)
                        },
                        modifier = Modifier.animateItem(),
                    )
                }
            }
            item(key = "weeklyProgress") {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                    shape = RoundedCornerShape(20.dp),
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.wellness_week_progress_title),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = stringResource(
                                R.string.wellness_week_progress_value,
                                completed,
                                eligible,
                            ),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        LinearProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    }
                }
            }
            item(key = "insight") {
                SupportingCard(
                    if (insight == null) {
                        stringResource(R.string.wellness_insight_more_data)
                    } else {
                        stringResource(
                            R.string.wellness_insight_sleep_energy,
                            insight.sampleDays,
                        )
                    },
                )
            }
            if (todayCheckIn?.hasDiscomfort == true) {
                item(key = "care") {
                    CareCard()
                }
            }
        }
    }

    if (showCheckIn && !uiState.isLoading) {
        DailyCheckInSheet(
            initial = uiState.checkIns.firstOrNull { it.localDate == editingDate },
            isSaving = writing || uiState.isCheckInWriting,
            saveFailed = writeFailed,
            onDismiss = { if (!writing) showCheckIn = false },
            onSave = { checkIn ->
                if (!writing) {
                    writing = true
                    writeFailed = false
                    scope.launch {
                        try {
                            if (onSaveCheckIn(checkIn)) {
                                showCheckIn = false
                                writing = false
                                snackbarHostState.showSnackbar(savedMessage)
                            } else {
                                writeFailed = true
                            }
                        } finally { writing = false }
                    }
                }
            },
        )
    }
    deletingDate?.let { date ->
        AlertDialog(
            onDismissRequest = { if (!writing) deletingDate = null },
            title = {
                Text(stringResource(R.string.morning_delete_title), style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface)
            },
            text = {
                Text(stringResource(if (writeFailed) R.string.morning_write_failed else R.string.morning_delete_message),
                    style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            },
            confirmButton = {
                TextButton(enabled = !writing && !uiState.isCheckInWriting, onClick = {
                    writing = true
                    writeFailed = false
                    scope.launch {
                        try {
                            if (onDeleteCheckIn(date)) deletingDate = null else writeFailed = true
                        } finally { writing = false }
                    }
                }) {
                    Text(stringResource(R.string.morning_delete), style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(enabled = !writing, onClick = { deletingDate = null }) {
                    Text(stringResource(R.string.morning_cancel), style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary)
                }
            },
        )
    }
}

@Composable
private fun CheckInCard(
    checkIn: DailyCheckIn?,
    privacyMode: Boolean,
    onClick: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(22.dp),
    ) {
        AnimatedContent(
            targetState = checkIn,
            transitionSpec = {
                fadeIn(tween(220)) togetherWith fadeOut(tween(120))
            },
            label = "checkInCard",
        ) {
            currentCheckIn ->
            Column(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = stringResource(R.string.morning_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                if (currentCheckIn == null) {
                    Text(
                        text = stringResource(R.string.morning_intro),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        ConditionSummary(
                            label = stringResource(R.string.wellness_condition_sleep),
                            value = currentCheckIn.sleep,
                            privacyMode = privacyMode,
                            modifier = Modifier.weight(1f),
                        )
                        ConditionSummary(
                            label = stringResource(R.string.morning_energy),
                            value = currentCheckIn.energy,
                            privacyMode = privacyMode,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
                Button(
                    onClick = onClick,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                ) {
                    Text(
                        text = stringResource(
                            if (currentCheckIn == null) {
                                R.string.morning_record
                            } else {
                                R.string.morning_edit
                            },
                        ),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        }
    }
}

@Composable
private fun ConditionSummary(
    label: String,
    value: Int?,
    privacyMode: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = if (privacyMode) {
                stringResource(R.string.wellness_hidden)
            } else {
                value?.toUiNumber() ?: stringResource(R.string.morning_unanswered)
            },
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun TodayActionCard(
    routine: Routine,
    isComplete: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardColor by animateColorAsState(
        targetValue = if (isComplete) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(260),
        label = "todayActionCardColor",
    )
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = cardColor,
        ),
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = routine.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = routineKindLabel(routine.kind),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            OutlinedButton(
                onClick = onToggle,
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
            ) {
                AnimatedContent(
                    targetState = isComplete,
                    transitionSpec = {
                        fadeIn(tween(180)) togetherWith fadeOut(tween(100))
                    },
                    label = "todayActionButton",
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
            AnimatedVisibility(
                visible = isComplete,
                enter = fadeIn(tween(220)) + expandVertically(tween(220)),
                exit = fadeOut(tween(120)) + shrinkVertically(tween(180)),
            ) {
                Text(
                    text = stringResource(R.string.wellness_action_feedback),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        }
    }
}

@Composable
internal fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onBackground,
    )
}

@Composable
internal fun SupportingCard(text: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(20.dp),
    ) {
        Text(
            text = text,
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun CareCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
        ),
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(R.string.wellness_care_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
            )
            Text(
                text = stringResource(R.string.wellness_care_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
            )
        }
    }
}

@Composable
internal fun routineKindLabel(kind: String): String =
    stringResource(
        when (runCatching { com.keelim.nandadiagnosis.wellness.domain.RoutineKind.valueOf(kind) }
            .getOrDefault(com.keelim.nandadiagnosis.wellness.domain.RoutineKind.CUSTOM)) {
            com.keelim.nandadiagnosis.wellness.domain.RoutineKind.SUPPLEMENT ->
                R.string.wellness_plan_category_supplement
            com.keelim.nandadiagnosis.wellness.domain.RoutineKind.RUNNING ->
                R.string.wellness_plan_category_cardio
            com.keelim.nandadiagnosis.wellness.domain.RoutineKind.EXERCISE ->
                R.string.wellness_plan_category_strength
            com.keelim.nandadiagnosis.wellness.domain.RoutineKind.SLEEP ->
                R.string.wellness_plan_category_sleep
            com.keelim.nandadiagnosis.wellness.domain.RoutineKind.STRESS ->
                R.string.wellness_plan_category_stress
            com.keelim.nandadiagnosis.wellness.domain.RoutineKind.ALCOHOL ->
                R.string.wellness_plan_category_alcohol
            com.keelim.nandadiagnosis.wellness.domain.RoutineKind.SMOKING ->
                R.string.wellness_plan_category_smoking
            com.keelim.nandadiagnosis.wellness.domain.RoutineKind.CUSTOM ->
                R.string.wellness_plan_category_custom
        },
    )
