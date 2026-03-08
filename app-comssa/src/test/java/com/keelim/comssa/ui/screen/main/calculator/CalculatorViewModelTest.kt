package com.keelim.comssa.ui.screen.main.calculator

import app.cash.turbine.test
import com.keelim.data.repository.calculator.CalculatorHistoryRepository
import com.keelim.model.CalculatorHistory
import com.keelim.model.CalculatorType
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class CalculatorViewModelTest : FunSpec({
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    extension(mainDispatcherRule)

    test("history maps repository entries to ui models") {
        runTest {
            val repository = FakeCalculatorHistoryRepository(
                initialHistory = listOf(
                    CalculatorHistory(
                        id = "history-1",
                        type = CalculatorType.TAX,
                        input = mapOf("income" to "1000"),
                        result = mapOf("tax" to "100"),
                        timestamp = 1234L,
                    ),
                ),
            )
            val viewModel = CalculatorViewModel(repository)

            viewModel.history.test {
                awaitItem() shouldBe emptyList()

                val history = awaitItem()
                history shouldHaveSize 1
                history.single() shouldBe CalculatorHistoryUi(
                    id = "history-1",
                    type = CalculatorTypeUi.TAX,
                    input = mapOf("income" to "1000"),
                    result = mapOf("tax" to "100"),
                    timestamp = 1234L,
                )
            }
        }
    }

    test("addHistory stores a new history entry") {
        runTest {
            val repository = FakeCalculatorHistoryRepository()
            val viewModel = CalculatorViewModel(repository)

            viewModel.history.test {
                awaitItem() shouldBe emptyList()

                viewModel.addHistory(
                    type = CalculatorTypeUi.LOAN_REPAYMENT,
                    input = mapOf("principal" to "100000"),
                    result = mapOf("monthlyPayment" to "2000"),
                )
                advanceUntilIdle()

                val history = awaitItem()
                history shouldHaveSize 1
                history.single().let { item ->
                    item.type shouldBe CalculatorTypeUi.LOAN_REPAYMENT
                    item.input shouldBe mapOf("principal" to "100000")
                    item.result shouldBe mapOf("monthlyPayment" to "2000")
                    item.id.isNotBlank() shouldBe true
                    (item.timestamp > 0L) shouldBe true
                }
            }
        }
    }

    test("clearHistory removes all stored history") {
        runTest {
            val repository = FakeCalculatorHistoryRepository(
                initialHistory = listOf(
                    CalculatorHistory(
                        id = "history-1",
                        type = CalculatorType.COMPOUND_INTEREST,
                        input = mapOf("principal" to "1000"),
                        result = mapOf("total" to "1100"),
                        timestamp = 999L,
                    ),
                ),
            )
            val viewModel = CalculatorViewModel(repository)

            viewModel.history.test {
                awaitItem() shouldBe emptyList()
                awaitItem().shouldHaveSize(1)

                viewModel.clearHistory()
                advanceUntilIdle()

                awaitItem() shouldBe emptyList()
                repository.clearCallCount shouldBe 1
            }
        }
    }
})

private class FakeCalculatorHistoryRepository(
    initialHistory: List<CalculatorHistory> = emptyList(),
) : CalculatorHistoryRepository {
    private val historyFlow = MutableStateFlow(initialHistory)

    var clearCallCount: Int = 0
        private set

    override fun getAllHistory(): Flow<List<CalculatorHistory>> = historyFlow.asStateFlow()

    override suspend fun addHistory(history: CalculatorHistory) {
        historyFlow.value = historyFlow.value + history
    }

    override suspend fun clearHistory() {
        clearCallCount += 1
        historyFlow.value = emptyList()
    }
}
