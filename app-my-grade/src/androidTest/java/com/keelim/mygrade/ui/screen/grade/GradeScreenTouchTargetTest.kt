package com.keelim.mygrade.ui.screen.grade

import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertWidthIsAtLeast
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.unit.dp
import com.keelim.core.designsystem.theme.KeelimDesignSystemTheme
import org.junit.Rule
import org.junit.Test

class GradeScreenTouchTargetTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun gradeActionsExposeAtLeast48DpTouchTargets() {
        composeRule.setContent {
            KeelimDesignSystemTheme {
                GradeContent(
                    subject = "Computer Science",
                    grade = "A",
                    rank = "1",
                    onNavigateNotes = {},
                    onEditClick = {},
                    onShareClick = {},
                )
            }
        }

        listOf("학점 메모 열기", "과목 점수 수정", "학점 결과 공유").forEach { description ->
            composeRule.onNodeWithContentDescription(description)
                .assertWidthIsAtLeast(48.dp)
                .assertHeightIsAtLeast(48.dp)
        }
    }
}
