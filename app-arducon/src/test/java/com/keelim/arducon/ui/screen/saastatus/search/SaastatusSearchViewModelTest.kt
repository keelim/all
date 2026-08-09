package com.keelim.arducon.ui.screen.saastatus.search

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class SaastatusSearchViewModelTest : FunSpec({

    test("검색어 업데이트가 정상적으로 작동해야 한다") {
        val viewModel = SaastatusSearchViewModel()

        viewModel.updateSearchQuery("status page")

        viewModel.searchQuery.value shouldBe "status page"
    }

    test("검색어 지우기 시 빈 문자열로 초기화되어야 한다") {
        val viewModel = SaastatusSearchViewModel()

        viewModel.updateSearchQuery("status page")
        viewModel.clearSearch()

        viewModel.searchQuery.value shouldBe ""
    }
})
