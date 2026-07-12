package com.keelim.nandadiagnosis.ui.screen.water

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.junit4.createComposeRule
import com.keelim.core.designsystem.theme.KeelimDesignSystemTheme
import com.keelim.model.DailyWaterTotal
import org.junit.Rule
import org.junit.Test

class WaterIntakeSemanticsTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun completedDailyGoalIsNotCommunicatedByColorAlone() {
        composeRule.setContent {
            KeelimDesignSystemTheme {
                WaterIntakeScreen(
                    todayTotal = 0,
                    dailyGoal = 2_000,
                    todayRecords = emptyList(),
                    weeklyHistory = listOf(
                        DailyWaterTotal(date = "2026-07-12", totalAmount = 2_000),
                    ),
                    onAddWater = {},
                    onDeleteRecord = {},
                )
            }
        }

        composeRule.onNode(
            SemanticsMatcher.expectValue(
                SemanticsProperties.StateDescription,
                "목표 달성",
            ),
        ).assertExists()
    }
}
