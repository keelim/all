package com.keelim.comssa.ui.screen.main.calendar

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class CalendarViewModelTest : FunSpec({
    test("can create calendar view model") {
        CalendarViewModel()::class.java shouldBe CalendarViewModel::class.java
    }
})
