package com.keelim.nandadiagnosis.wellness.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.nandadiagnosis.wellness.WellnessViewModel
import java.time.LocalDate

@Composable
fun WellnessRoute(
    viewModel: WellnessViewModel,
    canLoadAd: Boolean,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (!uiState.preferences.onboardingAccepted) {
        OnboardingGate(onAccept = viewModel::acceptOnboarding)
        return
    }

    WellnessScreen(
        uiState = uiState,
        canLoadAd = canLoadAd,
        onSaveMeasurement = { length, circumference, state ->
            viewModel.saveMeasurement(length, circumference, state)
        },
        onSetGoal = viewModel::setGoal,
        onClearGoal = viewModel::clearGoal,
        onAddRoutine = { name, kind ->
            viewModel.addRoutine(name, kind, LocalDate.now())
        },
        onSetRoutineCompletion = { routine, checked, duration ->
            viewModel.setRoutineCompletion(
                routine = routine,
                date = LocalDate.now(),
                checked = checked,
                duration = duration,
            )
        },
        onDeleteRoutine = viewModel::deleteRoutine,
    )
}
