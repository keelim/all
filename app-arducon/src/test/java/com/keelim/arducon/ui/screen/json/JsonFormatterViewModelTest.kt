package com.keelim.arducon.ui.screen.json

import com.keelim.data.json.JsonParser
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class JsonFormatterViewModelTest : FunSpec({

    lateinit var viewModel: JsonFormatterViewModel
    lateinit var jsonParser: JsonParser
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    extension(mainDispatcherRule)

    beforeTest {
        jsonParser = mockk()
        viewModel = JsonFormatterViewModel(jsonParser)
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
            val formatted = "{\n  \"name\": \"keelim\"\n}"
            every { jsonParser.formatJson(input) } returns formatted
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
            every { jsonParser.formatJson(input) } throws IllegalArgumentException("invalid json")
            viewModel.updateInputJson(input)

            viewModel.formatJson()

            viewModel.uiState.value shouldBe JsonFormatterUiState(
                inputJson = input,
                formattedJson = "",
                errorMessage = "invalid json",
            )
        }
    }

    test("clear가 입력과 포맷 결과를 초기화해야 한다") {
        runTest(testDispatcher) {
            val input = """{"name":"keelim"}"""
            every { jsonParser.formatJson(input) } returns "{}"
            viewModel.updateInputJson(input)
            viewModel.formatJson()

            viewModel.clear()

            viewModel.uiState.value shouldBe JsonFormatterUiState()
        }
    }
})
