package com.keelim.mygrade.ui.screen.analytics

import app.cash.turbine.test
import com.keelim.model.DailyStudyStats
import com.keelim.model.SubjectStudyStats
import com.keelim.mygrade.testutil.FakeStudyAnalyticsRepository
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class StudyAnalyticsViewModelTest : FunSpec({
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    extension(mainDispatcherRule)

    test("uiState combines repository streams into a loaded state") {
        runTest(testDispatcher) {
            val repository = FakeStudyAnalyticsRepository(
                dailyStats = listOf(DailyStudyStats(date = "2026-03-08", totalSeconds = 5400)),
                subjectStats = listOf(SubjectStudyStats(subject = "Math", totalSeconds = 5400)),
                totalStudySeconds = 5400,
                studyDaysCount = 4,
                currentStreak = 3,
            )
            val viewModel = StudyAnalyticsViewModel(repository)

            viewModel.uiState.test {
                awaitItem() shouldBe StudyAnalyticsUiState()
                advanceUntilIdle()
                awaitItem() shouldBe StudyAnalyticsUiState(
                    dailyStats = listOf(DailyStudyStats(date = "2026-03-08", totalSeconds = 5400)),
                    subjectStats = listOf(SubjectStudyStats(subject = "Math", totalSeconds = 5400)),
                    totalSeconds = 5400,
                    studyDaysCount = 4,
                    currentStreak = 3,
                    isLoading = false,
                )
            }
        }
    }

    test("recordSession delegates to the repository") {
        runTest(testDispatcher) {
            val repository = FakeStudyAnalyticsRepository()
            val viewModel = StudyAnalyticsViewModel(repository)

            viewModel.recordSession(subject = "Science", durationSeconds = 900)
            advanceUntilIdle()

            repository.recordedSessions shouldBe listOf("Science" to 900)
        }
    }
})
