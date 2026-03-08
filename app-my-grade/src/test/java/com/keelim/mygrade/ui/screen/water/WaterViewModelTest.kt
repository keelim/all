package com.keelim.mygrade.ui.screen.water

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class WaterViewModelTest : FunSpec({
    test("view model can be created") {
        WaterViewModel()::class.simpleName shouldBe "WaterViewModel"
    }
})
