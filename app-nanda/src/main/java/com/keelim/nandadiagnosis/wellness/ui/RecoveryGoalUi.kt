package com.keelim.nandadiagnosis.wellness.ui

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.keelim.model.wellness.DailyTimeBudget
import com.keelim.model.wellness.RecoveryGoal
import com.keelim.model.wellness.RecoveryGoalType
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.wellness.RecoveryRoutineDraft
import com.keelim.nandadiagnosis.wellness.domain.RecoveryActionTemplate
import com.keelim.nandadiagnosis.wellness.domain.RecoveryGoalRules

@Composable
internal fun RecoveryGoalCard(
    goal: RecoveryGoal?,
    weeklyActionCompletions: Int,
    weeklyActiveDays: Int,
    onChooseGoal: () -> Unit,
    onViewRecommendations: () -> Unit,
    onChangeGoal: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (goal == null) {
                Text(
                    text = stringResource(R.string.wellness_recovery_goal_empty_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Text(
                    text = stringResource(R.string.wellness_recovery_goal_empty_description),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Button(onClick = onChooseGoal) {
                    Text(
                        text = stringResource(R.string.wellness_recovery_goal_choose),
                        style = MaterialTheme.typography.labelLarge,
                        color = LocalContentColor.current,
                    )
                }
            } else {
                Text(
                    text = stringResource(goal.type.titleRes()),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Text(
                    text = stringResource(goal.type.descriptionRes()),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Text(
                    text = stringResource(
                        R.string.wellness_recovery_goal_weekly_activity,
                        pluralStringResource(
                            R.plurals.wellness_recovery_goal_action_count,
                            weeklyActionCompletions,
                            weeklyActionCompletions,
                        ),
                        pluralStringResource(
                            R.plurals.wellness_recovery_goal_active_day_count,
                            weeklyActiveDays,
                            weeklyActiveDays,
                        ),
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Button(
                        onClick = onViewRecommendations,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            text = stringResource(R.string.wellness_recovery_goal_view_actions),
                            style = MaterialTheme.typography.labelLarge,
                            color = LocalContentColor.current,
                        )
                    }
                    OutlinedButton(
                        onClick = onChangeGoal,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            text = stringResource(R.string.wellness_recovery_goal_change),
                            style = MaterialTheme.typography.labelLarge,
                            color = LocalContentColor.current,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecoveryGoalSheet(
    activeGoal: RecoveryGoal?,
    startWithRecommendations: Boolean,
    onDismiss: () -> Unit,
    onSave: (RecoveryGoalType, DailyTimeBudget, List<RecoveryRoutineDraft>) -> Unit,
) {
    var step by rememberSaveable(
        activeGoal?.type,
        activeGoal?.dailyTimeBudget,
        startWithRecommendations,
    ) {
        mutableStateOf(
            if (startWithRecommendations && activeGoal != null) {
                RecoveryGoalStep.RECOMMENDATIONS
            } else {
                RecoveryGoalStep.GOAL
            },
        )
    }
    var selectedGoal: RecoveryGoalType? by rememberSaveable(activeGoal?.type) {
        mutableStateOf(activeGoal?.type)
    }
    var selectedBudget: DailyTimeBudget? by rememberSaveable(activeGoal?.dailyTimeBudget) {
        mutableStateOf(activeGoal?.dailyTimeBudget)
    }
    var selectedActions by rememberSaveable(
        activeGoal?.type,
        activeGoal?.dailyTimeBudget,
        startWithRecommendations,
        stateSaver = recoveryActionSetSaver,
    ) {
        mutableStateOf(
            activeGoal?.let {
                RecoveryGoalRules.recommendations(it.type, it.dailyTimeBudget).toSet()
            }.orEmpty(),
        )
    }
    var selectedActionsGoal by rememberSaveable(
        activeGoal?.type,
        activeGoal?.dailyTimeBudget,
        startWithRecommendations,
    ) {
        mutableStateOf(activeGoal?.type)
    }
    var selectedActionsBudget by rememberSaveable(
        activeGoal?.type,
        activeGoal?.dailyTimeBudget,
        startWithRecommendations,
    ) {
        mutableStateOf(activeGoal?.dailyTimeBudget)
    }
    val recommendations = remember(selectedGoal, selectedBudget) {
        if (selectedGoal != null && selectedBudget != null) {
            RecoveryGoalRules.recommendations(selectedGoal!!, selectedBudget!!)
        } else {
            emptyList()
        }
    }
    val selectedDrafts = recommendations.filter(selectedActions::contains).map { action ->
        RecoveryRoutineDraft(
            template = action,
            name = stringResource(action.titleRes()),
        )
    }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            AnimatedContent(
                targetState = step,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "recoveryGoalStep",
            ) { currentStep ->
                when (currentStep) {
                    RecoveryGoalStep.GOAL -> GoalSelectionStep(
                        selectedGoal = selectedGoal,
                        onSelect = { selectedGoal = it },
                    )
                    RecoveryGoalStep.TIME -> TimeBudgetStep(
                        selectedBudget = selectedBudget,
                        onSelect = { selectedBudget = it },
                    )
                    RecoveryGoalStep.RECOMMENDATIONS -> RecommendationStep(
                        recommendations = recommendations,
                        selectedActions = selectedActions,
                        onToggle = { action ->
                            selectedActions = if (action in selectedActions) {
                                selectedActions - action
                            } else if (selectedActions.size < MAX_SELECTED_ACTIONS) {
                                selectedActions + action
                            } else {
                                selectedActions
                            }
                        },
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (step != RecoveryGoalStep.GOAL) {
                    TextButton(
                        onClick = {
                            step = when (step) {
                                RecoveryGoalStep.TIME -> RecoveryGoalStep.GOAL
                                RecoveryGoalStep.RECOMMENDATIONS -> RecoveryGoalStep.TIME
                                RecoveryGoalStep.GOAL -> RecoveryGoalStep.GOAL
                            }
                        },
                    ) {
                        Text(
                            text = stringResource(R.string.wellness_back),
                            style = MaterialTheme.typography.labelLarge,
                            color = LocalContentColor.current,
                        )
                    }
                }
                Button(
                    onClick = {
                        when (step) {
                            RecoveryGoalStep.GOAL -> step = RecoveryGoalStep.TIME
                            RecoveryGoalStep.TIME -> {
                                val goal = requireNotNull(selectedGoal)
                                val budget = requireNotNull(selectedBudget)
                                if (selectedActionsGoal != goal || selectedActionsBudget != budget) {
                                    selectedActions = recommendations.toSet()
                                    selectedActionsGoal = goal
                                    selectedActionsBudget = budget
                                }
                                step = RecoveryGoalStep.RECOMMENDATIONS
                            }
                            RecoveryGoalStep.RECOMMENDATIONS -> onSave(
                                requireNotNull(selectedGoal),
                                requireNotNull(selectedBudget),
                                selectedDrafts,
                            )
                        }
                    },
                    enabled = when (step) {
                        RecoveryGoalStep.GOAL -> selectedGoal != null
                        RecoveryGoalStep.TIME -> selectedBudget != null
                        RecoveryGoalStep.RECOMMENDATIONS ->
                            selectedGoal != null && selectedBudget != null
                    },
                ) {
                    Text(
                        text = stringResource(
                            if (step == RecoveryGoalStep.RECOMMENDATIONS) {
                                R.string.wellness_recovery_goal_save
                            } else {
                                R.string.wellness_next
                            },
                        ),
                        style = MaterialTheme.typography.labelLarge,
                        color = LocalContentColor.current,
                    )
                }
            }
        }
    }
}

@Composable
private fun GoalSelectionStep(
    selectedGoal: RecoveryGoalType?,
    onSelect: (RecoveryGoalType) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SheetStepHeader(
            stepText = stringResource(R.string.wellness_recovery_goal_step_one),
            title = stringResource(R.string.wellness_recovery_goal_select_title),
            description = stringResource(R.string.wellness_recovery_goal_select_description),
        )
        Column(
            modifier = Modifier.selectableGroup(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            RecoveryGoalType.entries.forEach { goalType ->
                RadioChoiceCard(
                    title = stringResource(goalType.titleRes()),
                    selected = selectedGoal == goalType,
                    onClick = { onSelect(goalType) },
                )
            }
        }
    }
}

@Composable
private fun TimeBudgetStep(
    selectedBudget: DailyTimeBudget?,
    onSelect: (DailyTimeBudget) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SheetStepHeader(
            stepText = stringResource(R.string.wellness_recovery_goal_step_two),
            title = stringResource(R.string.wellness_recovery_goal_time_title),
            description = stringResource(R.string.wellness_recovery_goal_time_description),
        )
        Column(
            modifier = Modifier.selectableGroup(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            DailyTimeBudget.entries.forEach { budget ->
                RadioChoiceCard(
                    title = stringResource(budget.titleRes()),
                    selected = selectedBudget == budget,
                    onClick = { onSelect(budget) },
                )
            }
        }
    }
}

@Composable
private fun RecommendationStep(
    recommendations: List<RecoveryActionTemplate>,
    selectedActions: Set<RecoveryActionTemplate>,
    onToggle: (RecoveryActionTemplate) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SheetStepHeader(
            stepText = stringResource(R.string.wellness_recovery_goal_step_three),
            title = stringResource(R.string.wellness_recovery_goal_actions_title),
            description = stringResource(R.string.wellness_recovery_goal_actions_description),
        )
        Text(
            text = stringResource(
                R.string.wellness_recovery_goal_selected_count,
                selectedActions.size,
            ),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            recommendations.forEach { action ->
                val selected = action in selectedActions
                val enabled = selected || selectedActions.size < MAX_SELECTED_ACTIONS
                CheckboxChoiceCard(
                    title = stringResource(action.titleRes()),
                    selected = selected,
                    enabled = enabled,
                    onClick = { onToggle(action) },
                )
            }
        }
        Text(
            text = stringResource(R.string.wellness_recovery_goal_actions_optional),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun SheetStepHeader(
    stepText: String,
    title: String,
    description: String,
) {
    Column(
        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = stepText,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun RadioChoiceCard(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val containerColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        label = "recoveryGoalRadioColor",
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton,
            ),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            RadioButton(selected = selected, onClick = null)
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = if (selected) {
                    MaterialTheme.colorScheme.onSecondaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
            )
        }
    }
}

@Composable
private fun CheckboxChoiceCard(
    title: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val containerColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        label = "recoveryGoalCheckboxColor",
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .toggleable(
                value = selected,
                enabled = enabled,
                role = Role.Checkbox,
                onValueChange = { onClick() },
            ),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Checkbox(checked = selected, enabled = enabled, onCheckedChange = null)
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = if (enabled) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
            )
        }
    }
}

@StringRes
private fun RecoveryGoalType.titleRes(): Int = when (this) {
    RecoveryGoalType.MORNING_ENERGY -> R.string.wellness_recovery_goal_morning_energy
    RecoveryGoalType.SLEEP_RHYTHM -> R.string.wellness_recovery_goal_sleep_rhythm
    RecoveryGoalType.EXERCISE_HABIT -> R.string.wellness_recovery_goal_exercise_habit
    RecoveryGoalType.CONFIDENCE_AND_SEXUAL_WELLNESS ->
        R.string.wellness_recovery_goal_confidence
    RecoveryGoalType.ALCOHOL_MANAGEMENT -> R.string.wellness_recovery_goal_alcohol
    RecoveryGoalType.SMOKING_CESSATION -> R.string.wellness_recovery_goal_smoking
    RecoveryGoalType.GENERAL_RECOVERY -> R.string.wellness_recovery_goal_general
}

@StringRes
private fun RecoveryGoalType.descriptionRes(): Int = when (this) {
    RecoveryGoalType.MORNING_ENERGY -> R.string.wellness_recovery_goal_morning_energy_description
    RecoveryGoalType.SLEEP_RHYTHM -> R.string.wellness_recovery_goal_sleep_rhythm_description
    RecoveryGoalType.EXERCISE_HABIT -> R.string.wellness_recovery_goal_exercise_habit_description
    RecoveryGoalType.CONFIDENCE_AND_SEXUAL_WELLNESS ->
        R.string.wellness_recovery_goal_confidence_description
    RecoveryGoalType.ALCOHOL_MANAGEMENT -> R.string.wellness_recovery_goal_alcohol_description
    RecoveryGoalType.SMOKING_CESSATION -> R.string.wellness_recovery_goal_smoking_description
    RecoveryGoalType.GENERAL_RECOVERY -> R.string.wellness_recovery_goal_general_description
}

@StringRes
private fun DailyTimeBudget.titleRes(): Int = when (this) {
    DailyTimeBudget.FIVE_MINUTES -> R.string.wellness_recovery_goal_time_five
    DailyTimeBudget.FIFTEEN_MINUTES -> R.string.wellness_recovery_goal_time_fifteen
    DailyTimeBudget.THIRTY_MINUTES_OR_MORE -> R.string.wellness_recovery_goal_time_thirty
}

@StringRes
private fun RecoveryActionTemplate.titleRes(): Int = when (this) {
    RecoveryActionTemplate.MORNING_SUNLIGHT_5 -> R.string.wellness_recovery_action_morning_sunlight_5
    RecoveryActionTemplate.AFTER_LUNCH_WALK_5 -> R.string.wellness_recovery_action_after_lunch_walk_5
    RecoveryActionTemplate.RECORD_WAKE_TIME -> R.string.wellness_recovery_action_record_wake_time
    RecoveryActionTemplate.AFTER_MEAL_WALK_10 -> R.string.wellness_recovery_action_after_meal_walk_10
    RecoveryActionTemplate.CONSISTENT_WAKE_WINDOW -> R.string.wellness_recovery_action_consistent_wake_window
    RecoveryActionTemplate.FULL_BODY_EXERCISE_10 -> R.string.wellness_recovery_action_full_body_exercise_10
    RecoveryActionTemplate.BRISK_WALK_20 -> R.string.wellness_recovery_action_brisk_walk_20
    RecoveryActionTemplate.FULL_BODY_STRENGTH_20 -> R.string.wellness_recovery_action_full_body_strength_20
    RecoveryActionTemplate.EARLY_BEDTIME_PREP_30 -> R.string.wellness_recovery_action_early_bedtime_prep_30
    RecoveryActionTemplate.CONSISTENT_WAKE_TIME -> R.string.wellness_recovery_action_consistent_wake_time
    RecoveryActionTemplate.AVOID_LATE_CAFFEINE -> R.string.wellness_recovery_action_avoid_late_caffeine
    RecoveryActionTemplate.SCREEN_FREE_20 -> R.string.wellness_recovery_action_screen_free_20
    RecoveryActionTemplate.WALK_5 -> R.string.wellness_recovery_action_walk_5
    RecoveryActionTemplate.FULL_BODY_STRENGTH_10 -> R.string.wellness_recovery_action_full_body_strength_10
    RecoveryActionTemplate.SQUAT_PUSHUP_ONE_SET -> R.string.wellness_recovery_action_squat_pushup_one_set
    RecoveryActionTemplate.FULL_BODY_STRENGTH_15 -> R.string.wellness_recovery_action_full_body_strength_15
    RecoveryActionTemplate.SECURE_SLEEP_TIME -> R.string.wellness_recovery_action_secure_sleep_time
    RecoveryActionTemplate.SLOW_BREATHING_5 -> R.string.wellness_recovery_action_slow_breathing_5
    RecoveryActionTemplate.ALCOHOL_FREE_TODAY -> R.string.wellness_recovery_action_alcohol_free_today
    RecoveryActionTemplate.NON_ALCOHOLIC_DRINK -> R.string.wellness_recovery_action_non_alcoholic_drink
    RecoveryActionTemplate.EVENING_WALK_10 -> R.string.wellness_recovery_action_evening_walk_10
    RecoveryActionTemplate.RECORD_ALCOHOL_USE -> R.string.wellness_recovery_action_record_alcohol_use
    RecoveryActionTemplate.DELAY_FIRST_SMOKING_30 -> R.string.wellness_recovery_action_delay_first_smoking_30
    RecoveryActionTemplate.CRAVING_WALK_5 -> R.string.wellness_recovery_action_craving_walk_5
    RecoveryActionTemplate.RECORD_SMOKING_USE -> R.string.wellness_recovery_action_record_smoking_use
    RecoveryActionTemplate.CHECK_SMOKING_SUPPORT -> R.string.wellness_recovery_action_check_smoking_support
    RecoveryActionTemplate.BRISK_WALK_10 -> R.string.wellness_recovery_action_brisk_walk_10
}

private enum class RecoveryGoalStep { GOAL, TIME, RECOMMENDATIONS }

private val recoveryActionSetSaver = listSaver<Set<RecoveryActionTemplate>, String>(
    save = { actions -> actions.map(RecoveryActionTemplate::name) },
    restore = { names ->
        names.mapNotNull { name ->
            RecoveryActionTemplate.entries.firstOrNull { it.name == name }
        }.toSet()
    },
)

private const val MAX_SELECTED_ACTIONS = 3
