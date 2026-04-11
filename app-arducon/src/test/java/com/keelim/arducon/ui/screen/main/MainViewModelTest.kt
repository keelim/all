package com.keelim.arducon.ui.screen.main

import app.cash.turbine.test
import com.keelim.data.repository.ArduconRepository
import com.keelim.model.DeepLink
import com.keelim.scheme.notification.SchemeNotificationManager
import com.keelim.testing.util.MainDispatcherRule
import dagger.Lazy
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest : FunSpec({

    lateinit var viewModel: MainViewModel
    lateinit var repository: ArduconRepository
    lateinit var schemeNotificationManager: SchemeNotificationManager
    lateinit var lazyNotificationManager: Lazy<SchemeNotificationManager>
    lateinit var categoriesFlow: MutableStateFlow<List<String>>
    lateinit var schemeFlow: MutableStateFlow<List<String>>
    lateinit var deepLinkFlow: MutableStateFlow<List<DeepLink>>
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    val bookmarkedDeepLink = DeepLink(
        url = "myapp://bookmark",
        timestamp = 1L,
        isBookMarked = true,
        title = "bookmark",
        category = "tools",
    )
    val unbookmarkedDeepLink = DeepLink(
        url = "myapp://plain",
        timestamp = 2L,
        isBookMarked = false,
        title = "plain",
        category = "tools",
    )
    val otherCategoryDeepLink = DeepLink(
        url = "myapp://other",
        timestamp = 3L,
        isBookMarked = false,
        title = "other",
        category = "other",
    )

    extension(mainDispatcherRule)

    beforeTest {
        repository = mockk(relaxed = true)
        schemeNotificationManager = mockk(relaxed = true)
        lazyNotificationManager = mockk()
        categoriesFlow = MutableStateFlow(listOf("zeta", "alpha"))
        schemeFlow = MutableStateFlow(listOf("myapp"))
        deepLinkFlow = MutableStateFlow(listOf(bookmarkedDeepLink, unbookmarkedDeepLink, otherCategoryDeepLink))

        every { repository.getCategories() } returns categoriesFlow
        every { repository.getSchemeList() } returns schemeFlow
        every { repository.getDeepLinkUrls("") } returns deepLinkFlow
        every { lazyNotificationManager.get() } returns schemeNotificationManager

        viewModel = MainViewModel(testDispatcher, repository, lazyNotificationManager)
    }

    test("categories는 정렬된 목록을 노출해야 한다") {
        runTest(testDispatcher) {
            viewModel.categories.test {
                awaitItem() shouldBe emptyList()
                advanceUntilIdle()
                awaitItem() shouldBe listOf("alpha", "zeta")
            }
        }
    }

    test("schemeList는 기본 스킴을 앞에 포함해야 한다") {
        runTest(testDispatcher) {
            viewModel.schemeList.test {
                awaitItem() shouldBe emptyList()
                advanceUntilIdle()
                awaitItem() shouldBe listOf("http", "https", "myapp")
            }
        }
    }

    test("deepLinkList는 카테고리별로 필터링된 북마크와 일반 링크를 분리해야 한다") {
        runTest(testDispatcher) {
            viewModel.deepLinkList.test {
                awaitItem() shouldBe Pair(emptyList(), emptyList())
                advanceUntilIdle()
                awaitItem() shouldBe Pair(
                    listOf(bookmarkedDeepLink),
                    listOf(unbookmarkedDeepLink, otherCategoryDeepLink),
                )

                viewModel.updateSelectedCategory("tools")
                advanceUntilIdle()

                awaitItem() shouldBe Pair(
                    listOf(bookmarkedDeepLink),
                    listOf(unbookmarkedDeepLink),
                )
            }
        }
    }

    test("onClickSearch는 딥링크를 저장하고 클릭 상태를 갱신해야 한다") {
        runTest(testDispatcher) {
            val captured = slot<DeepLink>()
            coEvery { repository.insertDeepLinkUrl(capture(captured)) } returns Unit

            viewModel.onClickSearch(
                uri = "myapp://home",
                title = "홈",
                category = "tools",
            )
            advanceUntilIdle()

            viewModel.onClickSearch.value shouldBe "myapp://home"
            captured.captured.url shouldBe "myapp://home"
            captured.captured.title shouldBe "홈"
            captured.captured.category shouldBe "tools"
            (captured.captured.timestamp > 0L) shouldBe true
        }
    }

    test("updateDeepLinkUrl는 북마크 상태를 토글해서 저장해야 한다") {
        runTest(testDispatcher) {
            viewModel.updateDeepLinkUrl(unbookmarkedDeepLink)
            advanceUntilIdle()

            coVerify {
                repository.updateDeepLinkUrl(
                    unbookmarkedDeepLink.copy(isBookMarked = true),
                )
            }
        }
    }

    test("showNotification은 SchemeNotificationManager에 위임해야 한다") {
        runTest(testDispatcher) {
            justRun {
                schemeNotificationManager.showDeepLinkNotification(
                    notificationId = 7,
                    title = "테스트",
                    message = "message",
                    deepLinkUri = "myapp://home",
                )
            }

            viewModel.showNotification(
                notificationId = 7,
                title = "테스트",
                message = "message",
                deepLinkUri = "myapp://home",
            )

            verify {
                schemeNotificationManager.showDeepLinkNotification(
                    notificationId = 7,
                    title = "테스트",
                    message = "message",
                    deepLinkUri = "myapp://home",
                )
            }
        }
    }

    test("recordDeepLinkUsage는 사용 횟수와 마지막 사용 시간을 갱신해야 한다") {
        runTest(testDispatcher) {
            val deepLink = unbookmarkedDeepLink.copy(usageCount = 2, lastUsed = 0L)

            viewModel.recordDeepLinkUsage(deepLink)
            advanceUntilIdle()

            coVerify {
                repository.updateDeepLinkUrl(
                    match {
                        it.url == deepLink.url &&
                            it.usageCount == 3 &&
                            it.lastUsed > 0L
                    },
                )
            }
        }
    }
})
