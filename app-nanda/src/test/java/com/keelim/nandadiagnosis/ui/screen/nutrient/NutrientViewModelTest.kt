package com.keelim.nandadiagnosis.ui.screen.nutrient

import app.cash.turbine.test
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class NutrientViewModelTest : FunSpec({
    lateinit var viewModel: NutrientViewModel
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    extension(mainDispatcherRule)

    beforeTest {
        viewModel = NutrientViewModel()
    }

    test("state emits nutrient loading and success data") {
        runTest(testDispatcher) {
            viewModel.state.test {
                awaitItem() shouldBe NutrientState.Empty
                advanceUntilIdle()

                awaitItem() shouldBe NutrientState.Loading
                val state = awaitItem() as NutrientState.Success
                state.items.size shouldBe 13
                state.items.first().first shouldBe "비타민 A"
                state.items.last().first shouldBe "비타민 K"

                cancelAndIgnoreRemainingEvents()
            }
        }
    }
})
