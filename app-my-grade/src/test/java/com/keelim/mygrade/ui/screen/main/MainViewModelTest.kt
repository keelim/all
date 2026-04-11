package com.keelim.mygrade.ui.screen.main

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class MainViewModelTest : FunSpec({
    test("updateEditType trims each field and clear resets all inputs") {
        val viewModel = MainViewModel()

        viewModel.updateEditType(EditType.Subject(" Math "))
        viewModel.updateEditType(EditType.Origin(" 95 "))
        viewModel.updateEditType(EditType.Average(" 80 "))
        viewModel.updateEditType(EditType.Number(" 5 "))
        viewModel.updateEditType(EditType.Student(" 30 "))

        viewModel.subject.value shouldBe "Math"
        viewModel.origin.value shouldBe "95"
        viewModel.average.value shouldBe "80"
        viewModel.number.value shouldBe "5"
        viewModel.student.value shouldBe "30"
        viewModel.mainScreenState.value shouldBe MainScreenState.empty()

        viewModel.clear()

        viewModel.subject.value shouldBe ""
        viewModel.origin.value shouldBe ""
        viewModel.average.value shouldBe ""
        viewModel.number.value shouldBe ""
        viewModel.student.value shouldBe ""
    }

    test("moveState replaces the current screen state") {
        val viewModel = MainViewModel()

        viewModel.moveState(MainState.Error(message = "invalid"))

        viewModel.state.value shouldBe MainState.Error(message = "invalid")
    }

    test("submit marks numeric fields as invalid when parsing fails") {
        val viewModel = MainViewModel()

        viewModel.updateEditType(EditType.Subject("Math"))
        viewModel.updateEditType(EditType.Origin("bad"))
        viewModel.updateEditType(EditType.Average("oops"))
        viewModel.updateEditType(EditType.Number("wrong"))
        viewModel.updateEditType(EditType.Student("NaN"))

        viewModel.submit()

        viewModel.mainScreenState.value shouldBe MainScreenState(
            originError = true,
            averageError = true,
            numberError = true,
            studentError = true,
        )
        viewModel.state.value shouldBe MainState.UnInitialized
    }

    test("submit emits a success state when every value is valid") {
        val viewModel = MainViewModel()

        viewModel.updateEditType(EditType.Subject("Math"))
        viewModel.updateEditType(EditType.Origin("95"))
        viewModel.updateEditType(EditType.Average("80"))
        viewModel.updateEditType(EditType.Number("5"))
        viewModel.updateEditType(EditType.Student("30"))

        viewModel.submit()

        viewModel.state.value shouldBe MainState.Success(
            flag = true,
            subject = "Math",
            value = Zvalue(((95f - 80f) / 5f).toDouble()).getNormalProbability(),
            student = 30,
        )
    }
})
