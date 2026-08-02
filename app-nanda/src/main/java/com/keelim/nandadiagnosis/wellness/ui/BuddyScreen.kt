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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.keelim.model.wellness.CareCheckIn
import com.keelim.model.wellness.CareCondition
import com.keelim.model.wellness.Routine
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.wellness.WellnessUiState
import com.keelim.model.wellness.DailyCheckIn
import com.keelim.nandadiagnosis.wellness.domain.InsightCalculator
import com.keelim.nandadiagnosis.wellness.domain.WellnessRules
import java.time.LocalDate
import kotlinx.coroutines.launch

@Composable
internal fun TodayScreen(
    uiState: WellnessUiState,
    privacyMode: Boolean,
    onSaveCheckIn: (DailyCheckIn) -> Boolean,
    onSaveCareCheckIn: (CareCondition, Boolean, Boolean, Boolean) -> Unit,
    onDeleteCheckIn: (String) -> Unit,
    onSetRoutineCompletion: (Routine, Boolean, Int?) -> Unit,
) {
    val today = remember { LocalDate.now() }
    val todayIso = today.toString()
    val todayCheckIn = uiState.checkIns.firstOrNull { it.localDate == todayIso }
    val todayCareCheckIn = uiState.careCheckIns.firstOrNull { it.localDate == todayIso }
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
    val careSavedMessage = stringResource(R.string.wellness_care_weather_saved)
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
            item(key = "careWeather") {
                CareWeatherSection(
                    date = today,
                    checkIn = todayCareCheckIn,
                    privacyMode = privacyMode,
                    onSave = { condition, sweat, friction, dryness ->
                        onSaveCareCheckIn(condition, sweat, friction, dryness)
                        scope.launch {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar(careSavedMessage)
                        }
                    },
                )
            }
            item(key = "checkIn") {
                CheckInCard(
                    checkIn = todayCheckIn,
                    privacyMode = privacyMode,
                    onClick = { showCheckIn = true },
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

    if (showCheckIn) {
        DailyCheckInSheet(
            initial = todayCheckIn,
            onDismiss = { showCheckIn = false },
            onDelete = todayCheckIn?.let { checkIn ->
                {
                    onDeleteCheckIn(checkIn.localDate)
                    showCheckIn = false
                }
            },
            onSave = { checkIn ->
                if (onSaveCheckIn(checkIn)) {
                    showCheckIn = false
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = savedMessage,
                        )
                    }
                }
            },
        )
    }
}

@Composable
private fun CareWeatherSection(
    date: LocalDate,
    checkIn: CareCheckIn?,
    privacyMode: Boolean,
    onSave: (CareCondition, Boolean, Boolean, Boolean) -> Unit,
) {
    var conditionName by remember(date, privacyMode) {
        mutableStateOf(checkIn?.condition?.name.takeUnless { privacyMode })
    }
    var sweat by remember(date, privacyMode) {
        mutableStateOf(checkIn?.sweat?.takeUnless { privacyMode } ?: false)
    }
    var friction by remember(date, privacyMode) {
        mutableStateOf(checkIn?.friction?.takeUnless { privacyMode } ?: false)
    }
    var dryness by remember(date, privacyMode) {
        mutableStateOf(checkIn?.dryness?.takeUnless { privacyMode } ?: false)
    }
    var hasLocalEdits by remember(date, privacyMode) { mutableStateOf(false) }
    val selectedCondition = conditionName?.let { name ->
        CareCondition.entries.firstOrNull { it.name == name }
    }

    LaunchedEffect(checkIn, privacyMode) {
        if (!hasLocalEdits) {
            conditionName = checkIn?.condition?.name.takeUnless { privacyMode }
            sweat = checkIn?.sweat?.takeUnless { privacyMode } ?: false
            friction = checkIn?.friction?.takeUnless { privacyMode } ?: false
            dryness = checkIn?.dryness?.takeUnless { privacyMode } ?: false
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = stringResource(R.string.wellness_care_weather_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = WellnessUiFormat.date(date),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Image(
            painter = painterResource(R.drawable.care_weather_orb),
            contentDescription = null,
            modifier = Modifier.size(228.dp),
        )
        Text(
            text = stringResource(R.string.wellness_care_weather_helper),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Row(
            modifier = Modifier.fillMaxWidth().selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            CareCondition.entries.forEach { condition ->
                val label = stringResource(
                    when (condition) {
                        CareCondition.COMFORTABLE -> R.string.wellness_care_comfortable
                        CareCondition.AWARE -> R.string.wellness_care_aware
                        CareCondition.NEEDS_ATTENTION -> R.string.wellness_care_needs_attention
                    },
                )
                val caption = stringResource(
                    when (condition) {
                        CareCondition.COMFORTABLE -> R.string.wellness_care_comfortable_caption
                        CareCondition.AWARE -> R.string.wellness_care_aware_caption
                        CareCondition.NEEDS_ATTENTION ->
                            R.string.wellness_care_needs_attention_caption
                    },
                )
                val icon = when (condition) {
                    CareCondition.COMFORTABLE -> Icons.Filled.CheckCircle
                    CareCondition.AWARE -> Icons.Filled.Info
                    CareCondition.NEEDS_ATTENTION -> Icons.Filled.Warning
                }
                CareConditionChoice(
                    label = label,
                    caption = caption,
                    icon = icon,
                    selected = selectedCondition == condition,
                    onClick = {
                        hasLocalEdits = true
                        conditionName = condition.name
                        onSave(condition, sweat, friction, dryness)
                    },
                    modifier = Modifier.weight(1f),
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(R.string.wellness_care_optional_tags),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            AnimatedVisibility(visible = selectedCondition == null) {
                Text(
                    text = stringResource(R.string.wellness_care_choose_condition),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                CareTagChip(
                    label = stringResource(R.string.wellness_care_tag_sweat),
                    selected = sweat,
                    enabled = selectedCondition != null,
                    onClick = {
                        selectedCondition?.let { condition ->
                            hasLocalEdits = true
                            sweat = !sweat
                            onSave(condition, sweat, friction, dryness)
                        }
                    },
                )
                CareTagChip(
                    label = stringResource(R.string.wellness_care_tag_friction),
                    selected = friction,
                    enabled = selectedCondition != null,
                    onClick = {
                        selectedCondition?.let { condition ->
                            hasLocalEdits = true
                            friction = !friction
                            onSave(condition, sweat, friction, dryness)
                        }
                    },
                )
                CareTagChip(
                    label = stringResource(R.string.wellness_care_tag_dryness),
                    selected = dryness,
                    enabled = selectedCondition != null,
                    onClick = {
                        selectedCondition?.let { condition ->
                            hasLocalEdits = true
                            dryness = !dryness
                            onSave(condition, sweat, friction, dryness)
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun CareConditionChoice(
    label: String,
    caption: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val accentColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.outlineVariant
        },
        animationSpec = tween(220),
        label = "careConditionAccent",
    )
    val containerColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(220),
        label = "careConditionContainer",
    )

    Column(
        modifier = modifier.selectable(
            selected = selected,
            role = Role.RadioButton,
            onClick = onClick,
        ).padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Surface(
            modifier = Modifier.size(88.dp),
            shape = CircleShape,
            color = containerColor,
            border = BorderStroke(if (selected) 2.dp else 1.dp, accentColor),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(38.dp),
                    tint = if (selected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                )
            }
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            textAlign = TextAlign.Center,
        )
        Text(
            text = caption,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun CareTagChip(
    label: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        enabled = enabled,
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
            }
        } else {
            null
        },
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = if (enabled) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                },
            )
        },
    )
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
                    text = stringResource(
                        if (currentCheckIn == null) {
                            R.string.wellness_checkin_title
                        } else {
                            R.string.wellness_checkin_complete
                        },
                    ),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                if (currentCheckIn == null) {
                    Text(
                        text = stringResource(R.string.wellness_checkin_description),
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
                            label = stringResource(R.string.wellness_condition_stress),
                            value = currentCheckIn.stress,
                            privacyMode = privacyMode,
                            modifier = Modifier.weight(1f),
                        )
                        ConditionSummary(
                            label = stringResource(R.string.wellness_condition_energy),
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
                                R.string.wellness_checkin_start
                            } else {
                                R.string.wellness_checkin_edit
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
    value: Int,
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
                value.toString()
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
