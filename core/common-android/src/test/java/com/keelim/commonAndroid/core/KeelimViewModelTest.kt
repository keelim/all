package com.keelim.commonAndroid.core

import app.cash.turbine.test
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest

private data class CounterState(val value: Int) : State
private data class IncrementAction(val delta: Int) : UserAction

private class CounterViewModel : KeelimViewModel<CounterState, IncrementAction>(CounterState(0)) {
    override suspend fun processUserAction(userAction: IncrementAction) {
        emitState(CounterState(userAction.delta))
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class KeelimViewModelTest : FunSpec({

    val testDispatcher = UnconfinedTestDispatcher()

    extension(MainDispatcherRule(testDispatcher))

    test("emitUserAction은 processUserAction을 거쳐 state를 갱신한다") {
        runTest {
            val viewModel = CounterViewModel()

            viewModel.state.test {
                awaitItem() shouldBe CounterState(0)

                viewModel.emitUserAction(IncrementAction(5))

                awaitItem() shouldBe CounterState(5)
            }
        }
    }
})
