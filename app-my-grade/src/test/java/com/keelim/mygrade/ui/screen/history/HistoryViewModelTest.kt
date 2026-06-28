package com.keelim.mygrade.ui.screen.history

import app.cash.turbine.test
import com.keelim.data.repository.HistoryRepository
import com.keelim.model.SimpleHistory
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryViewModelTest : FunSpec({
    val testDispatcher = StandardTestDispatcher()

    test("histories maps repository items to grade history models") {
        runTest(testDispatcher) {
            val repository = mockk<HistoryRepository>()
            every { repository.observeSimpleHistories() } returns flowOf(
                listOf(
                    SimpleHistory(
                        subject = "Math",
                        date = "2026-03-08T12:30:00",
                        grade = "A",
                        gradeRank = 1,
                        totalRank = 30,
                    ),
                ),
            )
            val viewModel = HistoryViewModel(repository, testDispatcher)

            viewModel.histories.test {
                awaitItem() shouldBe persistentListOf(
                    GradeHistory(
                        subject = "Math",
                        date = "2026.03.08",
                        grade = "A",
                        myGrade = 1,
                        totalStudent = 30,
                    ),
                )
                awaitComplete()
            }
        }
    }

    test("histories falls back to an empty list when the repository fails") {
        runTest(testDispatcher) {
            val repository = mockk<HistoryRepository>()
            every { repository.observeSimpleHistories() } returns flow {
                throw IllegalStateException("boom")
            }
            val viewModel = HistoryViewModel(repository, testDispatcher)

            viewModel.histories.test {
                awaitItem() shouldBe persistentListOf()
                awaitComplete()
            }
        }
    }
})
