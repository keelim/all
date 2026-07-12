package com.keelim.setting.screen.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import com.keelim.core.designsystem.theme.KeelimDesignSystemTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SettingsPressFeedbackTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun canceledPressRestoresBoundsAndDoesNotDispatchClick() {
        var clickCount = 0
        composeRule.mainClock.autoAdvance = false
        composeRule.setContent {
            KeelimDesignSystemTheme {
                CategoryItem(
                    title = "Theme",
                    icon = Icons.Default.Settings,
                    onClick = { clickCount += 1 },
                    modifier = Modifier.testTag("category"),
                )
            }
        }
        composeRule.mainClock.advanceTimeBy(1_000)
        val initialBounds = composeRule.onNodeWithTag("category")
            .fetchSemanticsNode().boundsInRoot

        composeRule.onNodeWithTag("category").performTouchInput {
            down(center)
            advanceEventTime(80)
            moveTo(Offset(-20f, -20f))
            advanceEventTime(80)
            up()
        }
        composeRule.mainClock.advanceTimeBy(1_000)

        val restoredBounds = composeRule.onNodeWithTag("category")
            .fetchSemanticsNode().boundsInRoot
        composeRule.runOnIdle { assertEquals(0, clickCount) }
        assertEquals(initialBounds.width, restoredBounds.width, 0.5f)
        assertEquals(initialBounds.height, restoredBounds.height, 0.5f)

        composeRule.onNodeWithTag("category").performClick()
        composeRule.runOnIdle { assertEquals(1, clickCount) }
    }
}
