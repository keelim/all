package com.keelim.comssa.ui.screen.main.flash

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class FlashCardViewModelTest : FunSpec({
    test("initial state starts on the front face") {
        val viewModel = FlashCardViewModel()

        viewModel.uiState.value.flashCardState shouldBe FlashCardState.Front
    }

    test("updateState toggles card face") {
        val viewModel = FlashCardViewModel()

        viewModel.updateState()
        viewModel.uiState.value.flashCardState shouldBe FlashCardState.Back

        viewModel.updateState()
        viewModel.uiState.value.flashCardState shouldBe FlashCardState.Front
    }
})
