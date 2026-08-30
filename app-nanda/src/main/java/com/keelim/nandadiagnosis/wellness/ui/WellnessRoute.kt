package com.keelim.nandadiagnosis.wellness.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.nandadiagnosis.wellness.WellnessViewModel

@Composable
fun WellnessRoute(viewModel: WellnessViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WellnessScreen(
        uiState = uiState,
        onSaveCheckIn = viewModel::saveCheckIn,
        onSaveMeasurement = viewModel::saveMeasurement,
        onAddRoutine = viewModel::addRoutine,
        onSaveRecoveryGoal = viewModel::saveRecoveryGoal,
        onSetRoutineCompletion = { routine, checked, duration ->
            viewModel.setRoutineCompletion(
                routine = routine,
                checked = checked,
                duration = duration,
            )
        },
        onDeleteRoutine = viewModel::deleteRoutine,
    )
}
