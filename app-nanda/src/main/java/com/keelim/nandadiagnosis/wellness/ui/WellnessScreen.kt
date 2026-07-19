@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.keelim.nandadiagnosis.wellness.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.wellness.WellnessUiState
import com.keelim.nandadiagnosis.wellness.GoalMetric
import com.keelim.nandadiagnosis.wellness.ads.RoutineAdBanner
import com.keelim.model.wellness.Routine
import com.keelim.model.wellness.RoutineCompletion
import com.keelim.nandadiagnosis.wellness.domain.MeasurementState
import com.keelim.nandadiagnosis.wellness.domain.RoutineKind
import com.keelim.nandadiagnosis.wellness.domain.SevenDaySummary
import com.keelim.nandadiagnosis.wellness.domain.WellnessRules
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private enum class WellnessTab {
    BUDDY,
    ROUTINE,
}

@Composable
internal fun WellnessScreen(
    uiState: WellnessUiState,
    canLoadAd: Boolean,
    onSaveMeasurement: (String, String, MeasurementState) -> Boolean,
    onSetGoal: (GoalMetric, String) -> Boolean,
    onClearGoal: () -> Unit,
    onAddRoutine: (String, RoutineKind) -> Boolean,
    onSetRoutineCompletion: (Routine, Boolean, Int?) -> Unit,
    onDeleteRoutine: (Routine) -> Unit,
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    var privacyMode by rememberSaveable { mutableStateOf(true) }
    val selectedTab = WellnessTab.entries[selectedTabIndex]

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            WellnessTopBar(
                privacyMode = privacyMode,
                onPrivacyModeChange = { privacyMode = !privacyMode },
            )
        },
        bottomBar = {
            Column {
                RoutineAdBanner(
                    canLoadAd = canLoadAd,
                    modifier = Modifier.fillMaxWidth(),
                )
                NavigationBar(containerColor = MaterialTheme.colorScheme.background) {
                    WellnessTab.entries.forEachIndexed { index, tab ->
                        val selected = selectedTabIndex == index
                        NavigationBarItem(
                            selected = selected,
                            onClick = { selectedTabIndex = index },
                            icon = {
                                Icon(
                                    imageVector =
                                        when (tab) {
                                            WellnessTab.BUDDY -> Icons.Filled.Home
                                            WellnessTab.ROUTINE -> Icons.Filled.DateRange
                                        },
                                    contentDescription = tab.label(),
                                )
                            },
                            label = {
                                Text(
                                    text = tab.label(),
                                    style = MaterialTheme.typography.labelMedium,
                                    color =
                                        if (selected) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                        },
                                )
                            },
                        )
                    }
                }
            }
        },
    ) { contentPadding ->
        AnimatedContent(
            targetState = selectedTab,
            transitionSpec = {
                fadeIn(animationSpec = tween(durationMillis = 160)) togetherWith
                    fadeOut(animationSpec = tween(durationMillis = 120))
            },
            label = "wellnessTab",
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
        ) { tab ->
            when (tab) {
                WellnessTab.BUDDY ->
                    BuddyScreen(
                        measurements = uiState.measurements,
                        goal = uiState.goal,
                        hasValidationError = "measurement" in uiState.validationErrors,
                        hasGoalValidationError = "goal" in uiState.validationErrors || "goalMeasurement" in uiState.validationErrors,
                        privacyMode = privacyMode,
                        onSaveMeasurement = onSaveMeasurement,
                        onSetGoal = onSetGoal,
                        onClearGoal = onClearGoal,
                    )

                WellnessTab.ROUTINE ->
                    RoutineScreen(
                        routines = uiState.routines,
                        completions = uiState.completions,
                        hasNameError = "routineName" in uiState.validationErrors,
                        hasDurationError = "duration" in uiState.validationErrors,
                        onAddRoutine = onAddRoutine,
                        onSetRoutineCompletion = onSetRoutineCompletion,
                        onDeleteRoutine = onDeleteRoutine,
                    )

            }
        }
    }
}

@Composable
private fun WellnessTopBar(
    privacyMode: Boolean,
    onPrivacyModeChange: () -> Unit,
) {
    val privacyLabel = stringResource(R.string.wellness_privacy_mode)

    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
            )
        },
        actions = {
            Text(
                text = WellnessUiFormat.date(LocalDate.now().toString()),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
            Surface(
                color =
                    if (privacyMode) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                shape = RoundedCornerShape(12.dp),
                modifier =
                    Modifier
                        .padding(end = 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(onClick = onPrivacyModeChange)
                        .semantics {
                            contentDescription = privacyLabel
                            role = Role.Switch
                        },
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = null,
                        tint =
                            if (privacyMode) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                    )
                    Text(
                        text = privacyLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                    )
                }
            }
        },
    )
}

@Composable
private fun WellnessTab.label(): String =
    stringResource(
        when (this) {
            WellnessTab.BUDDY -> R.string.wellness_tab_buddy
            WellnessTab.ROUTINE -> R.string.wellness_tab_routine
        },
    )

@Composable
internal fun RoutineCard(
    routine: Routine,
    todayCompletion: RoutineCompletion?,
    summary: SevenDaySummary,
    onSetCompletion: (Boolean, Int?) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val kind = remember(routine.kind) { RoutineKind.valueOf(routine.kind) }
    val isCompleted = todayCompletion != null
    var durationText by
        rememberSaveable(routine.id) {
            mutableStateOf(todayCompletion?.durationMinutes?.toString().orEmpty())
        }
    val completionDescription =
        stringResource(R.string.wellness_routine_completion_description, routine.name)
    val deleteDescription =
        stringResource(R.string.wellness_routine_delete_description, routine.name)

    LaunchedEffect(todayCompletion?.durationMinutes) {
        todayCompletion?.durationMinutes?.let { durationText = it.toString() }
    }

    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = routine.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = kind.label(),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                TextButton(
                    onClick = onDelete,
                    modifier = Modifier.semantics { contentDescription = deleteDescription },
                ) {
                    Text(
                        text = stringResource(R.string.wellness_routine_delete),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = isCompleted,
                    onCheckedChange = { checked ->
                        onSetCompletion(checked, durationText.toIntOrNull())
                    },
                    modifier = Modifier.semantics { contentDescription = completionDescription },
                )
                Text(
                    text = stringResource(R.string.wellness_routine_today_complete),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            AnimatedVisibility(visible = kind != RoutineKind.SUPPLEMENT) {
                OutlinedTextField(
                    value = durationText,
                    onValueChange = { input ->
                        durationText = input.filter(Char::isDigit).take(4)
                        if (isCompleted) {
                            onSetCompletion(true, durationText.toIntOrNull())
                        }
                    },
                    label = {
                        Text(
                            text = stringResource(R.string.wellness_routine_duration_label),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    },
                    suffix = {
                        Text(
                            text = stringResource(R.string.wellness_minute_suffix),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    },
                    supportingText = {
                        Text(
                            text =
                                stringResource(
                                    R.string.wellness_routine_duration_help,
                                    WellnessUiFormat.number(1),
                                    WellnessUiFormat.number(1_440),
                                ),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Text(
                text =
                    stringResource(
                        R.string.wellness_routine_summary,
                        WellnessUiFormat.number(7),
                        WellnessUiFormat.number(summary.completedDays),
                        WellnessUiFormat.number(summary.eligibleDays),
                    ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
internal fun MeasurementState.label(): String =
    stringResource(
        when (this) {
            MeasurementState.RELAXED -> R.string.wellness_measurement_state_relaxed
            MeasurementState.STRETCHED -> R.string.wellness_measurement_state_stretched
            MeasurementState.MAXIMUM -> R.string.wellness_measurement_state_maximum
        },
    )

@Composable
internal fun measurementStateLabel(state: String): String =
    runCatching { MeasurementState.valueOf(state) }
        .getOrDefault(MeasurementState.RELAXED)
        .label()

@Composable
internal fun RoutineKind.label(): String =
    stringResource(
        when (this) {
            RoutineKind.SUPPLEMENT -> R.string.wellness_routine_kind_supplement
            RoutineKind.RUNNING -> R.string.wellness_routine_kind_running
            RoutineKind.EXERCISE -> R.string.wellness_routine_kind_exercise
        },
    )

internal object WellnessUiFormat {
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

    fun date(isoLocalDate: String): String =
        runCatching { LocalDate.parse(isoLocalDate).format(dateFormatter) }
            .getOrDefault(isoLocalDate)

    fun number(value: Number): String =
        NumberFormat.getNumberInstance(Locale.getDefault())
            .apply { maximumFractionDigits = 1 }
            .format(value)

}
