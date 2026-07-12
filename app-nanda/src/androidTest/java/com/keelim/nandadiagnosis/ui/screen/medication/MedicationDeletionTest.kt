package com.keelim.nandadiagnosis.ui.screen.medication

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodes
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.keelim.core.designsystem.theme.KeelimDesignSystemTheme
import com.keelim.data.model.Medication
import kotlinx.collections.immutable.toImmutableList
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class MedicationDeletionTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun deletionRunsOnceAfterExitAndKeepsTheCorrectKeyedItem() {
        var deleteCalls = 0
        composeRule.mainClock.autoAdvance = false

        composeRule.setContent {
            val medications = remember {
                mutableStateListOf(
                    Medication("first", "First", "1", 9, 0),
                    Medication("second", "Second", "1", 10, 0),
                )
            }
            KeelimDesignSystemTheme {
                MedicationScreen(
                    medications = medications.toImmutableList(),
                    showAddDialog = false,
                    editingMedication = null,
                    onToggleMedication = {},
                    onAddMedication = { _, _, _, _ -> },
                    onUpdateMedication = { _, _, _, _ -> },
                    onRemoveMedication = { medication ->
                        deleteCalls += 1
                        medications.remove(medication)
                    },
                    onEditMedication = {},
                    onShowAddDialog = {},
                    onHideAddDialog = {},
                )
            }
        }

        composeRule.onAllNodes(
            hasContentDescription("복약 알림 삭제") and hasClickAction(),
        ).onFirst().performClick()
        composeRule.runOnIdle { assertEquals(0, deleteCalls) }

        composeRule.mainClock.advanceTimeBy(1_000)
        composeRule.waitForIdle()

        composeRule.runOnIdle { assertEquals(1, deleteCalls) }
        composeRule.onNodeWithText("First").assertDoesNotExist()
        composeRule.onNodeWithText("Second").assertExists()

        composeRule.mainClock.advanceTimeBy(1_000)
        composeRule.runOnIdle { assertEquals(1, deleteCalls) }
    }
}
