package com.keelim.nandadiagnosis.wellness.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.common.platform.privacy.PrivacySettings
import com.keelim.nandadiagnosis.wellness.WellnessViewModel
import com.keelim.nandadiagnosis.wellness.morning.MorningSignalViewModel
import java.time.Duration

@Composable
fun WellnessRoute(
    canLoadAd: Boolean,
    privacySettings: PrivacySettings = PrivacySettings(
        appLockEnabled = false,
        obscureRecentApps = true,
        blockScreenshots = true,
        autoLockTimeout = Duration.ofMinutes(5),
    ),
    onUpdatePrivacySettings: (PrivacySettings) -> Unit = {},
    viewModel: WellnessViewModel = hiltViewModel(),
    morningSignalViewModel: MorningSignalViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WellnessScreen(
        uiState = uiState,
        onSaveCheckIn = viewModel::saveCheckIn,
        onSaveCareCheckIn = viewModel::saveCareCheckIn,
        onDeleteCheckIn = viewModel::deleteCheckIn,
        onSaveMeasurement = viewModel::saveMeasurement,
        onAddRoutine = viewModel::addRoutine,
        onSetRoutineCompletion = { routine, checked, duration ->
            viewModel.setRoutineCompletion(
                routine = routine,
                checked = checked,
                duration = duration,
            )
        },
        onDeleteRoutine = viewModel::deleteRoutine,
        canLoadAd = canLoadAd,
        privacySettings = privacySettings,
        onUpdatePrivacySettings = onUpdatePrivacySettings,
        morningSignalViewModel = morningSignalViewModel,
    )
}
