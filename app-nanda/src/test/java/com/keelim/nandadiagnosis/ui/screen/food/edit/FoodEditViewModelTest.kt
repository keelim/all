package com.keelim.nandadiagnosis.ui.screen.food.edit

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class FoodEditViewModelTest : FunSpec({
    test("viewModel can be created") {
        val viewModel = FoodEditViewModel()

        viewModel.javaClass.simpleName shouldBe "FoodEditViewModel"
    }
})
