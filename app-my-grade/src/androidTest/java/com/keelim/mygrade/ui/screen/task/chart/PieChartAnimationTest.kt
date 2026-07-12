package com.keelim.mygrade.ui.screen.task.chart

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.keelim.composeutil.component.canvas.chart.PieChart
import com.keelim.composeutil.component.canvas.chart.PieChartEntry
import com.keelim.core.designsystem.theme.KeelimDesignSystemTheme
import org.junit.Rule
import org.junit.Test

class PieChartAnimationTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun ordinaryRecompositionDoesNotRestartCompletedSweep() {
        val recompositionTrigger = mutableIntStateOf(0)
        composeRule.mainClock.autoAdvance = false

        composeRule.setContent {
            recompositionTrigger.intValue
            KeelimDesignSystemTheme {
                PieChart(
                    entries = listOf(
                        PieChartEntry(name = "Complete", color = Color.Blue, percentage = 1f),
                    ),
                    radiusOuter = 40.dp,
                    chartBarWidth = 8.dp,
                    duration = 300,
                    modifier = Modifier.testTag("pieChart"),
                )
            }
        }

        composeRule.mainClock.advanceTimeBy(400)
        val completeMatcher = SemanticsMatcher.expectValue(
            SemanticsProperties.ProgressBarRangeInfo,
            ProgressBarRangeInfo(1f, 0f..1f),
        )
        composeRule.onNode(completeMatcher).assertExists()

        composeRule.runOnIdle { recompositionTrigger.intValue += 1 }
        composeRule.mainClock.advanceTimeByFrame()

        composeRule.onNode(completeMatcher).assertExists()
    }
}
