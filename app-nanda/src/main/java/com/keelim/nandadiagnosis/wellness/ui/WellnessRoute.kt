package com.keelim.nandadiagnosis.wellness.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.nandadiagnosis.wellness.WellnessViewModel
import java.time.LocalDate

@Composable
fun WellnessRoute(
    viewModel: WellnessViewModel,
    canRequestAds: Boolean,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (!uiState.preferences.onboardingAccepted) {
        OnboardingGate(onAccept = viewModel::acceptOnboarding)
        return
    }

    WellnessScreen(
        uiState = uiState,
        showRoutineAd = canRequestAds,
        onSaveMeasurement = { length, circumference, state ->
            viewModel.saveMeasurement(length, circumference, state)
        },
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
