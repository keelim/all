package com.keelim.arducon.ui.screen.ogtag

import com.keelim.data.repository.linkinspector.LinkInspectorRepository
import com.keelim.model.linkinspector.OgResult
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class OgTagPreviewViewModelTest : FunSpec({

    lateinit var viewModel: OgTagPreviewViewModel
    lateinit var repository: LinkInspectorRepository
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    extension(mainDispatcherRule)

    beforeTest {
        repository = mockk()
        viewModel = OgTagPreviewViewModel(testDispatcher, repository)
    }

    test("fetchOg 결과를 OgTagData 로 매핑해야 한다") {
        runTest {
            val url = "https://example.com"
            coEvery { repository.fetchOg(url) } returns OgResult(
                title = "Example title",
                description = "Example description",
                image = "https://example.com/image.png",
            )

            var actual: OgTagData? = null

            viewModel.parseOgTags(url) { data ->
                actual = data
            }
            advanceUntilIdle()

            actual shouldBe OgTagData(
                title = "Example title",
                description = "Example description",
                imageUrl = "https://example.com/image.png",
            )
        }
    }

    test("repository 가 null 을 반환하면 빈 OgTagData 를 내려야 한다") {
        runTest {
            val url = "https://example.com"
            coEvery { repository.fetchOg(url) } returns null

            var actual: OgTagData? = null

            viewModel.parseOgTags(url) { data ->
                actual = data
            }
            advanceUntilIdle()

            actual shouldBe OgTagData()
        }
    }
})
