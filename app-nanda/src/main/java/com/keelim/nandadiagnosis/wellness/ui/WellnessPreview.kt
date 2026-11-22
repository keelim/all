package com.keelim.nandadiagnosis.wellness.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.keelim.model.wellness.DailyCheckIn
import com.keelim.model.wellness.Routine
import com.keelim.model.wellness.RoutineCompletion
import com.keelim.nandadiagnosis.ui.theme.NandaTheme
import com.keelim.nandadiagnosis.wellness.WellnessUiState
import com.keelim.nandadiagnosis.wellness.domain.RoutineKind
import com.keelim.nandadiagnosis.wellness.morning.MorningSignalSettings
import com.keelim.nandadiagnosis.wellness.morning.MorningSignalUiState
import java.time.LocalDate

@Preview(name = "Today · no records", showBackground = true, widthDp = 412, heightDp = 892, locale = "ko")
@Composable
private fun TodayNoRecordsPreview() {
    WellnessPreviewShell(
        state = sampleWellnessUiState.copy(checkIns = emptyList(), careCheckIns = emptyList()),
    )
}

@Preview(name = "Today · privacy on", showBackground = true, widthDp = 412, heightDp = 892, locale = "ko")
@Composable
private fun TodayPopulatedPrivacyPreview() {
    WellnessPreviewShell(
        state = sampleWellnessUiState,
    )
}

@Preview(name = "Today · care weather", showBackground = true, widthDp = 412, heightDp = 892, locale = "ko")
@Composable
private fun TodayOverviewPreview() {
    WellnessPreviewShell(
        state = sampleWellnessUiState,
        todayListState = rememberLazyListState(initialFirstVisibleItemIndex = 1),
    )
}

@Preview(name = "Plan", showBackground = true, widthDp = 412, heightDp = 892, locale = "ko")
@Composable
private fun PlanPreview() {
    WellnessPreviewShell(state = sampleWellnessUiState, initialTab = WellnessDestination.PLAN)
}

@Preview(name = "Insights · synthetic trend", showBackground = true, widthDp = 412, heightDp = 892, locale = "ko")
@Composable
private fun InsightsPreview() {
    WellnessPreviewShell(
        state = sampleWellnessUiState.copy(checkIns = sampleTrend),
        initialTab = WellnessDestination.INSIGHTS,
        initialPrivacyMode = false,
    )
}

@Preview(name = "Tools", showBackground = true, widthDp = 412, heightDp = 892, locale = "ko")
@Composable
private fun ToolsPreview() {
    WellnessPreviewShell(state = sampleWellnessUiState, initialTab = WellnessDestination.TOOLS)
}

@Preview(name = "Tools · measurements", showBackground = true, widthDp = 412, heightDp = 892, locale = "ko")
@Composable
private fun MeasurementToolPreview() {
    WellnessPreviewShell(
        state = sampleWellnessUiState,
        initialTab = WellnessDestination.TOOLS,
        initialToolsPreviewDestination = ToolsPreviewDestination.MEASUREMENTS,
    )
}

@Preview(name = "Tools · morning signal", showBackground = true, widthDp = 412, heightDp = 892, locale = "ko")
@Composable
private fun MorningSignalPreview() {
    WellnessPreviewShell(
        state = sampleWellnessUiState,
        initialTab = WellnessDestination.TOOLS,
        initialToolsPreviewDestination = ToolsPreviewDestination.MORNING_SIGNAL,
        morningSignalPreviewState = MorningSignalUiState(MorningSignalSettings(enabled = true, hour = 7, minute = 30)),
    )
}

@Preview(name = "Daily check-in sheet", showBackground = true, widthDp = 412, heightDp = 892, locale = "ko")
@Composable
private fun DailyCheckInSheetPreview() {
    WellnessPreviewShell(state = sampleWellnessUiState, showCheckInSheet = true, checkInSheetStep = 1)
}

@Preview(name = "Daily check-in details", showBackground = true, widthDp = 412, heightDp = 892, locale = "ko")
@Composable
private fun DailyCheckInDetailsSheetPreview() {
    WellnessPreviewShell(state = sampleWellnessUiState, showCheckInSheet = true, checkInSheetStep = 2)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WellnessPreviewShell(
    state: WellnessUiState,
    initialTab: WellnessDestination = WellnessDestination.TODAY,
    initialPrivacyMode: Boolean = true,
    initialToolsPreviewDestination: ToolsPreviewDestination? = null,
    morningSignalPreviewState: MorningSignalUiState? = null,
    todayListState: LazyListState? = null,
    showCheckInSheet: Boolean = false,
    checkInSheetStep: Int = 1,
) {
    NandaTheme {
        Box(Modifier.fillMaxSize()) {
            WellnessScreen(
                uiState = state,
                onSaveCheckIn = { true },
                onSaveCareCheckIn = { _, _, _, _ -> },
                onDeleteCheckIn = {},
                onSaveMeasurement = { _, _, _ -> true },
                onAddRoutine = { _, _ -> true },
                onSetRoutineCompletion = { _, _, _ -> },
                onDeleteRoutine = {},
                initialTab = initialTab,
                initialPrivacyMode = initialPrivacyMode,
                initialToolsPreviewDestination = initialToolsPreviewDestination,
                morningSignalPreviewState = morningSignalPreviewState,
                todayListState = todayListState,
            )
            if (showCheckInSheet) {
                DailyCheckInSheet(
                    initial = null,
                    initialStep = checkInSheetStep,
                    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                    onDismiss = {},
                    onDelete = null,
                    onSave = {},
                )
            }
        }
    }
}

private val previewToday = LocalDate.now()

private val sampleWellnessUiState = WellnessUiState(
    routines = listOf(
        Routine(
            id = 1,
            name = "30분 산책",
            kind = RoutineKind.RUNNING.name,
            createdLocalDate = previewToday.minusDays(6).toString(),
        ),
        Routine(
            id = 2,
            name = "자정 전 취침 준비",
            kind = RoutineKind.SLEEP.name,
            createdLocalDate = previewToday.minusDays(6).toString(),
        ),
    ),
    completions = listOf(
        RoutineCompletion(routineId = 1, localDate = previewToday.minusDays(4).toString()),
        RoutineCompletion(routineId = 1, localDate = previewToday.minusDays(2).toString()),
        RoutineCompletion(routineId = 1, localDate = previewToday.toString()),
        RoutineCompletion(routineId = 2, localDate = previewToday.minusDays(1).toString()),
    ),
    checkIns = listOf(
        DailyCheckIn(
            localDate = previewToday.toString(),
            sleep = 4,
            stress = 2,
            energy = 4,
            desire = 3,
            confidence = 3,
        ),
    ),
)

private val sampleTrend = (0..6).map { offset ->
    DailyCheckIn(
        localDate = previewToday.minusDays((6 - offset).toLong()).toString(),
        sleep = if (offset < 4) 4 else 3,
        stress = 4 - offset % 3,
        energy = 4,
        desire = 2 + offset % 2,
        confidence = 3 + offset % 2,
    )
}
