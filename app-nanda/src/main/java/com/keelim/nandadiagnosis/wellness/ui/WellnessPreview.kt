package com.keelim.nandadiagnosis.wellness.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.keelim.model.wellness.Routine
import com.keelim.nandadiagnosis.wellness.WellnessUiState
import com.keelim.nandadiagnosis.wellness.domain.DailyCheckIn
import com.keelim.nandadiagnosis.wellness.domain.RoutineKind

@Preview(showBackground = true, backgroundColor = 0xFF071625)
@Composable
private fun WellnessPreview() {
    MaterialTheme {
        WellnessScreen(
            uiState = sampleWellnessUiState,
            onSaveCheckIn = { true },
            onSaveMeasurement = { _, _, _ -> true },
            onAddRoutine = { _, _ -> true },
            onSaveRecoveryGoal = { _, _, _ -> },
            onSetRoutineCompletion = { _, _, _ -> },
            onDeleteRoutine = {},
        )
    }
}

internal val sampleWellnessUiState =
    WellnessUiState(
        routines = listOf(
            Routine(
                id = 1,
                name = "30-minute walk",
                kind = RoutineKind.RUNNING.name,
                createdLocalDate = "2026-07-20",
            ),
            Routine(
                id = 2,
                name = "Prepare for bed before midnight",
                kind = RoutineKind.SLEEP.name,
                createdLocalDate = "2026-07-20",
            ),
        ),
        checkIns = listOf(
            DailyCheckIn(
                localDate = "2026-07-26",
                sleep = 4,
                stress = 2,
                energy = 4,
                desire = 3,
                confidence = 3,
            ),
        ),
    )
