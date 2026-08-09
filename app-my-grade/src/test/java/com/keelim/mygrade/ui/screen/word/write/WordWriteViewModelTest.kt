package com.keelim.mygrade.ui.screen.word.write

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class WordWriteViewModelTest : FunSpec({
    test("view model can be created") {
        WordWriteViewModel()::class.simpleName shouldBe "WordWriteViewModel"
    }
})
