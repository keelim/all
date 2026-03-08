package com.keelim.cnubus.ui.screen.main

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class MainViewModelTest : FunSpec({

    test("뷰모델이 생성되어야 한다") {
        val viewModel = MainViewModel()

        viewModel::class.java.simpleName shouldBe "MainViewModel"
    }
})
