package com.keelim.mygrade.ui.screen.word.show

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class WordShowViewModelTest : FunSpec({
    test("view model can be created") {
        WordShowViewModel()::class.simpleName shouldBe "WordShowViewModel"
    }
})
