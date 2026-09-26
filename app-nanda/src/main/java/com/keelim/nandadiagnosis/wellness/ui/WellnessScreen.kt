package com.keelim.nandadiagnosis.wellness.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.res.stringResource
import com.keelim.model.wellness.DailyTimeBudget
import com.keelim.model.wellness.RecoveryGoalType
import com.keelim.model.wellness.Routine
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.wellness.RecoveryRoutineDraft
import com.keelim.nandadiagnosis.wellness.WellnessUiState
import com.keelim.nandadiagnosis.wellness.domain.DailyCheckIn
import com.keelim.nandadiagnosis.wellness.domain.MeasurementState
import com.keelim.nandadiagnosis.wellness.domain.RoutineKind
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@Composable
internal fun WellnessScreen(
    uiState: WellnessUiState,
    onSaveCheckIn: suspend (DailyCheckIn) -> Boolean,
    onDeleteCheckIn: suspend (String) -> Boolean = { false },
    onSaveMeasurement: (String, String, MeasurementState) -> Boolean,
    onAddRoutine: (String, RoutineKind) -> Boolean,
    onSaveRecoveryGoal: (
        RecoveryGoalType,
        DailyTimeBudget,
        List<RecoveryRoutineDraft>,
    ) -> Unit,
    onSetRoutineCompletion: (Routine, Boolean, Int?) -> Unit,
    onDeleteRoutine: (Routine) -> Unit,
    canRequestAds: Boolean = false,
    privacyOptionsRequired: Boolean = false,
    onShowPrivacyOptions: () -> Unit = {},
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    var privacyMode by rememberSaveable { mutableStateOf(true) }
    val selectedTab = WellnessDestination.entries[selectedTabIndex]

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            WellnessTopBar(
                privacyMode = privacyMode,
                onPrivacyModeChange = { privacyMode = !privacyMode },
            )
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                WellnessDestination.entries.forEachIndexed { index, destination ->
                    val selected = selectedTabIndex == index
                    NavigationBarItem(
                        selected = selected,
                        onClick = { selectedTabIndex = index },
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = stringResource(destination.labelRes),
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(destination.labelRes),
                                style = MaterialTheme.typography.labelMedium,
                                color = if (selected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                },
                            )
                        },
                    )
                }
            }
        },
    ) { contentPadding ->
        AnimatedContent(
            targetState = selectedTab,
            transitionSpec = {
                val direction = if (targetState.ordinal > initialState.ordinal) 1 else -1
                (
                    slideInHorizontally(tween(220)) { width -> direction * width / 8 } +
                        fadeIn(tween(180))
                ) togetherWith (
                    slideOutHorizontally(tween(180)) { width -> -direction * width / 10 } +
                        fadeOut(tween(140))
                )
            },
            label = "wellnessDestination",
            modifier = Modifier.fillMaxSize().padding(contentPadding),
        ) { destination ->
            when (destination) {
                WellnessDestination.TODAY -> TodayScreen(
                    uiState = uiState,
                    privacyMode = privacyMode,
                    onSaveCheckIn = onSaveCheckIn,
                    onDeleteCheckIn = onDeleteCheckIn,
                    onSetRoutineCompletion = onSetRoutineCompletion,
                )

                WellnessDestination.PLAN -> PlanScreen(
                    uiState = uiState,
                    canRequestAds = canRequestAds,
                    onAddRoutine = onAddRoutine,
                    onSaveRecoveryGoal = onSaveRecoveryGoal,
                    onSetRoutineCompletion = onSetRoutineCompletion,
                    onDeleteRoutine = onDeleteRoutine,
                )

                WellnessDestination.INSIGHTS -> InsightsScreen(
                    uiState = uiState,
                    privacyMode = privacyMode,
                )

                WellnessDestination.TOOLS -> ToolsScreen(
                    uiState = uiState,
                    privacyMode = privacyMode,
                    onPrivacyModeChange = { privacyMode = it },
                    onSaveMeasurement = onSaveMeasurement,
                    privacyOptionsRequired = privacyOptionsRequired,
                    onShowPrivacyOptions = onShowPrivacyOptions,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WellnessTopBar(
    privacyMode: Boolean,
    onPrivacyModeChange: () -> Unit,
) {
    val privacyState = stringResource(
        if (privacyMode) R.string.wellness_privacy_on else R.string.wellness_privacy_off,
    )
    val privacyTint by animateColorAsState(
        targetValue = if (privacyMode) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        animationSpec = tween(220),
        label = "privacyTint",
    )
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        actions = {
            Text(
                text = WellnessUiFormat.date(LocalDate.now()),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            IconButton(
                onClick = onPrivacyModeChange,
                modifier = Modifier.semantics {
                    role = Role.Switch
                    stateDescription = privacyState
                    toggleableState = ToggleableState(privacyMode)
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = stringResource(R.string.wellness_privacy_mode),
                    tint = privacyTint,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
    )
}

internal object WellnessUiFormat {
    private val dateFormatter by lazy {
        DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
            .withLocale(Locale.getDefault())
    }

    fun date(date: LocalDate): String = dateFormatter.format(date)
}
