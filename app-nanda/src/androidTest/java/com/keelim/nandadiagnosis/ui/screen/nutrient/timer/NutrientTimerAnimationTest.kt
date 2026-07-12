package com.keelim.nandadiagnosis.ui.screen.nutrient.timer

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.junit4.createComposeRule
import com.keelim.core.designsystem.theme.KeelimDesignSystemTheme
import org.junit.Rule
import org.junit.Test

class NutrientTimerAnimationTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun ordinaryRecompositionKeepsInterpolatedProgress() {
        val viewModel = NutrientTimerViewModel().apply {
            second = 10
            leftTime.intValue = 10
        }
        val dialogState = mutableStateOf(false)
        val recompositionTrigger = mutableIntStateOf(0)
        composeRule.mainClock.autoAdvance = false

        composeRule.setContent {
            recompositionTrigger.intValue
            KeelimDesignSystemTheme {
                CircularCountDownTimer(
                    runningState = RunningState.STARTED,
                    viewModel = viewModel,
                    dialogState = dialogState,
                    addedTime = "10:00",
                )
            }
        }

        composeRule.runOnIdle { viewModel.leftTime.intValue = 9 }
        composeRule.mainClock.advanceTimeBy(1_000)
        val ninetyPercent = SemanticsMatcher.expectValue(
            SemanticsProperties.ProgressBarRangeInfo,
            ProgressBarRangeInfo(0.9f, 0f..1f),
        )
        composeRule.onNode(ninetyPercent).assertExists()

        composeRule.runOnIdle { recompositionTrigger.intValue += 1 }
        composeRule.mainClock.advanceTimeByFrame()

        composeRule.onNode(ninetyPercent).assertExists()
    }
}
