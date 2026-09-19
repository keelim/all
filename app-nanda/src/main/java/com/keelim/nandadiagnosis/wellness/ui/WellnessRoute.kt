package com.keelim.nandadiagnosis.wellness.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.keelim.nandadiagnosis.wellness.WellnessViewModel

@Composable
fun WellnessRoute(
    viewModel: WellnessViewModel = hiltViewModel(),
    canRequestAds: Boolean = false,
    privacyOptionsRequired: Boolean = false,
    onShowPrivacyOptions: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    androidx.compose.runtime.LaunchedEffect(lifecycleOwner, viewModel) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
            while (true) {
                viewModel.refreshToday()
                kotlinx.coroutines.delay(60_000)
            }
        }
    }
    WellnessScreen(
        uiState = uiState,
        onSaveCheckIn = viewModel::saveCheckIn,
        onDeleteCheckIn = viewModel::deleteCheckIn,
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
        canRequestAds = canRequestAds,
        privacyOptionsRequired = privacyOptionsRequired,
        onShowPrivacyOptions = onShowPrivacyOptions,
    )
}
