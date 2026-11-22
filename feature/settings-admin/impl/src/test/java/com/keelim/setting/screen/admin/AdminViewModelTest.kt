package com.keelim.setting.screen.admin

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.types.shouldBeInstanceOf

class AdminViewModelTest : FunSpec({
    test("AdminViewModel can be constructed") {
        AdminViewModel().shouldBeInstanceOf<AdminViewModel>()
    }
})
