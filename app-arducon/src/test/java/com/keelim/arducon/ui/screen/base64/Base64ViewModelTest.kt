package com.keelim.arducon.ui.screen.base64

import android.util.Base64
import com.keelim.core.data.repository.Base64Repository
import com.keelim.shared.data.database.model.Base64History
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class Base64ViewModelTest : FunSpec({

    lateinit var viewModel: Base64ViewModel
    lateinit var repository: Base64Repository
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    extension(mainDispatcherRule)

    beforeTest {
        mockkStatic(Base64::class)
        repository = mockk(relaxed = true)
        every { repository.getAllHistory() } returns flowOf(emptyList())

        viewModel = Base64ViewModel(repository)
    }

    afterTest {
        unmockkStatic(Base64::class)
    }

    test("processBase64가 인코딩 결과와 이력을 업데이트해야 한다") {
        runTest(testDispatcher) {
            every { Base64.encodeToString("hello".toByteArray(), Base64.DEFAULT) } returns "aGVsbG8=\n"
            viewModel.updateInputText("hello")

            viewModel.processBase64()
            advanceUntilIdle()

            viewModel.uiState.value shouldBe Base64UiState(
                inputText = "hello",
                outputText = "aGVsbG8=",
                selectedIndex = 0,
                errorMessage = null,
            )
            coVerify { repository.insertHistory("hello", true) }
        }
    }

    test("processBase64가 디코딩 결과와 이력을 업데이트해야 한다") {
        runTest(testDispatcher) {
            every { Base64.decode("aGVsbG8=", Base64.DEFAULT) } returns "hello".toByteArray()
            viewModel.updateSelectedIndex(1)
            viewModel.updateInputText("aGVsbG8=")

            viewModel.processBase64()
            advanceUntilIdle()

            viewModel.uiState.value shouldBe Base64UiState(
                inputText = "aGVsbG8=",
                outputText = "hello",
                selectedIndex = 1,
                errorMessage = null,
            )
            coVerify { repository.insertHistory("aGVsbG8=", false) }
        }
    }

    test("유효하지 않은 Base64 문자열이면 오류를 노출하고 이력을 저장하지 않아야 한다") {
        runTest(testDispatcher) {
            every { Base64.decode("not-base64", Base64.DEFAULT) } throws IllegalArgumentException("bad base64")
            viewModel.updateSelectedIndex(1)
            viewModel.updateInputText("not-base64")

            viewModel.processBase64()
            advanceUntilIdle()

            viewModel.uiState.value.outputText shouldBe ""
            viewModel.uiState.value.errorMessage shouldBe "bad base64"
            coVerify(exactly = 0) { repository.insertHistory(any(), any()) }
        }
    }

    test("deleteHistory가 저장소 삭제를 위임해야 한다") {
        runTest(testDispatcher) {
            val history = Base64History(uid = 1, text = "hello", isEncoded = true, timestamp = 100L)

            viewModel.deleteHistory(history)
            advanceUntilIdle()

            coVerify { repository.deleteHistory(history) }
        }
    }

    test("clear는 선택 탭을 유지하면서 입력과 결과를 초기화해야 한다") {
        runTest(testDispatcher) {
            viewModel.updateSelectedIndex(1)
            viewModel.updateInputText("aGVsbG8=")
            viewModel.processBase64()
            advanceUntilIdle()

            viewModel.clear()

            viewModel.uiState.value shouldBe Base64UiState(selectedIndex = 1)
        }
    }
})
