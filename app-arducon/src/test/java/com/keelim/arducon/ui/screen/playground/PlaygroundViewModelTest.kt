package com.keelim.arducon.ui.screen.playground

import com.keelim.data.repository.linkinspector.LinkInspectorRepository
import com.keelim.model.linkinspector.HttpResult
import com.keelim.model.linkinspector.OgResult
import com.keelim.model.linkinspector.ResolvedApp
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class PlaygroundViewModelTest : FunSpec({

    lateinit var viewModel: PlaygroundViewModel
    lateinit var repository: LinkInspectorRepository
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    extension(mainDispatcherRule)

    beforeTest {
        repository = mockk()
        viewModel = PlaygroundViewModel(repository)
    }

    test("파라미터를 모두 입력하면 preview 에 쿼리 문자열이 포함되어야 한다") {
        viewModel.updateUrl("https://example.com/path")
        viewModel.updateParamKey("utm_source")
        viewModel.updateParamValue("newsletter")

        viewModel.uiState.value.preview shouldBe "https://example.com/path?utm_source=newsletter"
    }

    test("validate 는 preview URL 로 검사하고 결과 텍스트를 구성해야 한다") {
        runTest {
            val previewUrl = "https://example.com/path?utm_source=newsletter"
            val resolvedApps = listOf(ResolvedApp(label = "Chrome", packageName = "com.android.chrome"))
            val httpResult = HttpResult(statusCode = 200, finalUrl = "https://final.example.com")
            val ogResult = OgResult(
                title = "Example title",
                description = "Example description",
                image = "https://example.com/image.png",
            )

            coEvery { repository.resolveApps(previewUrl) } returns resolvedApps
            coEvery { repository.checkHttp(previewUrl) } returns httpResult
            coEvery { repository.fetchOg(httpResult.finalUrl) } returns ogResult

            viewModel.updateUrl("https://example.com/path")
            viewModel.updateParamKey("utm_source")
            viewModel.updateParamValue("newsletter")

            viewModel.validate()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            state.preview shouldBe previewUrl
            state.resolvedApps shouldBe resolvedApps
            state.http shouldBe httpResult
            state.og shouldBe ogResult
            state.isLoading shouldBe false
            state.error shouldBe null
            state.resultText shouldContain "Resolved Apps (1):"
            state.resultText shouldContain " - Chrome (com.android.chrome)"
            state.resultText shouldContain "HTTP: 200 → https://final.example.com"
            state.resultText shouldContain "OG: Example title | Example description"

            coVerify(exactly = 1) { repository.resolveApps(previewUrl) }
            coVerify(exactly = 1) { repository.checkHttp(previewUrl) }
            coVerify(exactly = 1) { repository.fetchOg(httpResult.finalUrl) }
        }
    }
})
