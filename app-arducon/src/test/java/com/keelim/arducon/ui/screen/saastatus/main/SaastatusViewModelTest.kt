package com.keelim.arducon.ui.screen.saastatus.main

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class SaastatusViewModelTest : FunSpec({

    test("초기 상태는 빈 목록이어야 한다") {
        val viewModel = SaastatusViewModel()

        viewModel.state.value shouldBe emptyList()
    }
})
