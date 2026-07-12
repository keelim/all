package com.keelim.setting.screen.theme

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import com.keelim.core.designsystem.theme.KeelimDesignSystemTheme
import com.keelim.shared.data.model.ThemeType
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ThemeScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun selectingThemeUpdatesSelectedSemantics() {
        val selectedTheme = mutableStateOf(ThemeType.LIGHT)
        val selectedMatcher = SemanticsMatcher.expectValue(SemanticsProperties.Selected, true)
        val unselectedMatcher = SemanticsMatcher.expectValue(SemanticsProperties.Selected, false)

        composeRule.setContent {
            KeelimDesignSystemTheme {
                ThemeScreen(
                    themeTypeState = ThemeTypeState(selectedRadio = selectedTheme.value),
                    onThemeSelect = { selectedTheme.value = it },
                )
            }
        }

        composeRule.onAllNodes(selectedMatcher).assertCountEquals(1)
        composeRule.onAllNodes(unselectedMatcher).assertCountEquals(1).onFirst().performClick()

        composeRule.runOnIdle {
            assertEquals(ThemeType.DARK, selectedTheme.value)
        }
        composeRule.onAllNodes(selectedMatcher).assertCountEquals(1)
    }
}
