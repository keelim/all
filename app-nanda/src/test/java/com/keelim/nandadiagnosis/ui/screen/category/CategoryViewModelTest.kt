package com.keelim.nandadiagnosis.ui.screen.category

import app.cash.turbine.test
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryViewModelTest : FunSpec({
    lateinit var viewModel: CategoryViewModel
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    extension(mainDispatcherRule)

    beforeTest {
        viewModel = CategoryViewModel()
    }

    test("state emits the expected category list") {
        runTest(testDispatcher) {
            viewModel.state.test {
                awaitItem() shouldBe CategoryState.Empty
                advanceUntilIdle()

                val state = awaitItem() as CategoryState.Success
                state.items.size shouldBe 13
                state.items.first() shouldBe "건강증진"
                state.items.last() shouldBe "성장/발달"

                cancelAndIgnoreRemainingEvents()
            }
        }
    }
})
