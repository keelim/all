package com.keelim.comssa.ui.screen.notification

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodes
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.keelim.core.designsystem.theme.KeelimDesignSystemTheme
import com.keelim.data.model.MarketSchedule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class MarketNotificationDeletionTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun deletionRunsOnceAfterExitAndKeepsTheCorrectKeyedItem() {
        var deleteCalls = 0
        composeRule.mainClock.autoAdvance = false

        composeRule.setContent {
            val schedules = remember {
                mutableStateListOf(
                    MarketSchedule("first", "First", 9, 0),
                    MarketSchedule("second", "Second", 10, 0),
                )
            }
            KeelimDesignSystemTheme {
                LazyColumn {
                    items(items = schedules, key = { it.id }) { schedule ->
                        MarketScheduleItem(
                            schedule = schedule,
                            onToggle = {},
                            onDelete = {
                                deleteCalls += 1
                                schedules.remove(schedule)
                            },
                        )
                    }
                }
            }
        }

        composeRule.onAllNodes(
            SemanticsMatcher.expectValue(
                SemanticsProperties.StateDescription,
                "알림 켜짐",
            ),
        ).assertCountEquals(2)

        composeRule.onAllNodes(
            hasContentDescription("Delete") and hasClickAction(),
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
