package com.keelim.mysenior

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.keelim.composeutil.navigation.KeelimNavDisplay
import com.keelim.core.designsystem.theme.KeelimDesignSystemTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class KeelimNavDisplayDirectionTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun forwardAndBackTransitionsMoveInOppositeDirections() {
        val backStack = mutableStateListOf("first")
        composeRule.mainClock.autoAdvance = false
        composeRule.setContent {
            KeelimDesignSystemTheme {
                KeelimNavDisplay(backStack = backStack) {
                    entry<String> { route ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag(route),
                        )
                    }
                }
            }
        }
        composeRule.mainClock.advanceTimeBy(1_000)

        composeRule.runOnIdle { backStack += "second" }
        composeRule.mainClock.advanceTimeByFrame()
        val forwardLeft = composeRule.onNodeWithTag("second")
            .fetchSemanticsNode().boundsInRoot.left
        assertTrue("Forward destination should enter from the right", forwardLeft > 0f)

        composeRule.mainClock.advanceTimeBy(1_000)
        composeRule.runOnIdle { backStack.removeLast() }
        composeRule.mainClock.advanceTimeByFrame()
        val backLeft = composeRule.onNodeWithTag("first")
            .fetchSemanticsNode().boundsInRoot.left
        assertTrue("Back destination should enter from the left", backLeft < 0f)
    }
}
