package com.keelim.arducon.ui.screen.json

import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json

@OptIn(ExperimentalCoroutinesApi::class)
class JsonFormatterViewModelTest : FunSpec({

    lateinit var viewModel: JsonFormatterViewModel
    lateinit var json: Json
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    extension(mainDispatcherRule)

    beforeTest {
        json = Json {
            prettyPrint = true
            prettyPrintIndent = "    "
        }
        viewModel = JsonFormatterViewModel(json)
    }

    test("updateInputJson이 입력 상태를 갱신해야 한다") {
        runTest(testDispatcher) {
            viewModel.updateInputJson("""{"name":"keelim"}""")

            viewModel.uiState.value shouldBe JsonFormatterUiState(
                inputJson = """{"name":"keelim"}""",
                formattedJson = "",
                errorMessage = null,
            )
        }
    }

    test("formatJson이 성공하면 포맷된 결과를 노출해야 한다") {
        runTest(testDispatcher) {
            val input = """{"name":"keelim"}"""
            val formatted = "{\n    \"name\": \"keelim\"\n}"
            viewModel.updateInputJson(input)

            viewModel.formatJson()

            viewModel.uiState.value shouldBe JsonFormatterUiState(
                inputJson = input,
                formattedJson = formatted,
                errorMessage = null,
            )
        }
    }

    test("formatJson이 실패하면 오류 메시지를 노출해야 한다") {
        runTest(testDispatcher) {
            val input = """{"name":}"""
            viewModel.updateInputJson(input)

            viewModel.formatJson()

            viewModel.uiState.value.inputJson shouldBe input
            viewModel.uiState.value.formattedJson shouldBe ""
            viewModel.uiState.value.errorMessage.isNullOrBlank() shouldBe false
        }
    }

    test("clear가 입력과 포맷 결과를 초기화해야 한다") {
        runTest(testDispatcher) {
            val input = """{"name":"keelim"}"""
            viewModel.updateInputJson(input)
            viewModel.formatJson()

            viewModel.clear()

            viewModel.uiState.value shouldBe JsonFormatterUiState()
        }
    }
})
