package com.keelim.arducon.ui.screen.stats

import app.cash.turbine.test
import com.keelim.data.repository.ArduconRepository
import com.keelim.model.DeepLink
import com.keelim.model.UsageStat
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class StatsViewModelTest : FunSpec({

    lateinit var viewModel: StatsViewModel
    lateinit var repository: ArduconRepository
    lateinit var topUsedLinksFlow: MutableStateFlow<List<DeepLink>>
    lateinit var recentUsedLinksFlow: MutableStateFlow<List<DeepLink>>
    lateinit var dailyUsageStatsFlow: MutableStateFlow<List<UsageStat>>
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    extension(mainDispatcherRule)

    beforeTest {
        repository = mockk()
        topUsedLinksFlow = MutableStateFlow(emptyList())
        recentUsedLinksFlow = MutableStateFlow(emptyList())
        dailyUsageStatsFlow = MutableStateFlow(emptyList())

        every { repository.getTopUsedLinks(limit = 5) } returns topUsedLinksFlow
        every { repository.getRecentUsedLinks(limit = 10) } returns recentUsedLinksFlow
        every { repository.getDailyUsageStats(limit = 7) } returns dailyUsageStatsFlow

        viewModel = StatsViewModel(repository)
    }

    test("repository 에 정의된 limit 으로 통계 flow 를 구독해야 한다") {
        verify(exactly = 1) { repository.getTopUsedLinks(limit = 5) }
        verify(exactly = 1) { repository.getRecentUsedLinks(limit = 10) }
        verify(exactly = 1) { repository.getDailyUsageStats(limit = 7) }
    }

    test("topUsedLinks 는 repository 값을 그대로 반영해야 한다") {
        runTest {
            val topLink = DeepLink(
                url = "https://example.com/top",
                timestamp = 1L,
                title = "Top link",
                usageCount = 12,
            )

            viewModel.topUsedLinks.test {
                awaitItem() shouldBe emptyList()

                topUsedLinksFlow.value = listOf(topLink)
                advanceUntilIdle()

                awaitItem() shouldBe listOf(topLink)
            }
        }
    }

    test("recentUsedLinks 는 repository 값을 그대로 반영해야 한다") {
        runTest {
            val recentLink = DeepLink(
                url = "https://example.com/recent",
                timestamp = 2L,
                title = "Recent link",
                usageCount = 3,
            )

            viewModel.recentUsedLinks.test {
                awaitItem() shouldBe emptyList()

                recentUsedLinksFlow.value = listOf(recentLink)
                advanceUntilIdle()

                awaitItem() shouldBe listOf(recentLink)
            }
        }
    }

    test("dailyUsageStats 는 repository 값을 그대로 반영해야 한다") {
        runTest {
            val usageStat = UsageStat(day = "2026-03-08", count = 5)

            viewModel.dailyUsageStats.test {
                awaitItem() shouldBe emptyList()

                dailyUsageStatsFlow.value = listOf(usageStat)
                advanceUntilIdle()

                awaitItem() shouldBe listOf(usageStat)
            }
        }
    }
})
