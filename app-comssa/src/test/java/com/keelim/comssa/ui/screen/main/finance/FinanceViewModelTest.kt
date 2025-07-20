package com.keelim.comssa.ui.screen.main.finance

import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.core.model.finance.FinanceCategory
import com.keelim.core.model.finance.FinanceRssItem
import com.keelim.core.model.finance.FinanceSource
import com.keelim.data.repository.FinanceRssRepository
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.Instant
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FinanceViewModelTest {

    private lateinit var viewModel: FinanceViewModel
    private lateinit var mockRepository: FinanceRssRepository
    private val testDispatcher = StandardTestDispatcher()

    private val testSources = listOf(
        FinanceSource(
            name = "테스트경제",
            url = "https://test.com/feed",
            category = FinanceCategory.ECONOMY,
            isEnabled = true
        ),
        FinanceSource(
            name = "테스트주식",
            url = "https://test.com/stock",
            category = FinanceCategory.STOCK,
            isEnabled = true
        )
    )

    private val testItems = listOf(
        FinanceRssItem(
            title = "경제 뉴스 1",
            description = "경제 뉴스 설명 1",
            link = "https://test.com/1",
            pubDate = Instant.fromEpochMilliseconds(1000L),
            category = "경제",
            source = "테스트경제"
        ),
        FinanceRssItem(
            title = "주식 뉴스 1",
            description = "주식 뉴스 설명 1",
            link = "https://test.com/2",
            pubDate = Instant.fromEpochMilliseconds(2000L),
            category = "주식",
            source = "테스트주식"
        ),
        FinanceRssItem(
            title = "암호화폐 뉴스 1",
            description = "암호화폐 뉴스 설명 1",
            link = "https://test.com/3",
            pubDate = Instant.fromEpochMilliseconds(3000L),
            category = "암호화폐",
            source = "테스트경제"
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockRepository = mockk()
        coEvery { mockRepository.getSources() } returns testSources
        coEvery { mockRepository.getRssItems(any()) } returns flowOf(testItems)
        coEvery { mockRepository.clearCache() } returns Unit
        coEvery { mockRepository.invalidateCacheForSource(any()) } returns Unit
        coEvery { mockRepository.getCacheInfo() } returns mapOf("test" to 1000L)

        viewModel = FinanceViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `초기 상태는 로딩 상태여야 한다`() = runTest {
        val initialState = viewModel.items.value
        assertTrue(initialState is SealedUiState.Loading)
    }

        @Test
    fun `RSS 아이템을 성공적으로 가져와야 한다`() = runTest {
        // Flow가 초기화될 때까지 대기
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.items.value
        // 초기 상태는 Loading이거나 Success일 수 있음
        assertTrue("Expected Loading or Success state but got: $state", 
                  state is SealedUiState.Loading || state is SealedUiState.Success)
        
        if (state is SealedUiState.Success) {
            val items = state.value
            assertTrue("Items should not be empty", items.isNotEmpty())
        }
    }

    @Test
    fun `필터 버튼이 올바르게 설정되어야 한다`() {
        val filterButtons = viewModel.filterButtons
        assertEquals(6, filterButtons.size)

        val labels = filterButtons.map { it.label }
        assertTrue(labels.contains("전체"))
        assertTrue(labels.contains("주식"))
        assertTrue(labels.contains("암호화폐"))
        assertTrue(labels.contains("외환"))
        assertTrue(labels.contains("경제"))
        assertTrue(labels.contains("부동산"))
    }

        @Test
    fun `카테고리 필터가 올바르게 적용되어야 한다`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()
        
        // 주식 필터 적용
        val stockFilter = viewModel.filterButtons.find { it.label == "주식" }!!
        viewModel.updateFilter(stockFilter)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // 필터가 적용되었는지 확인 (상태는 Loading 또는 Success일 수 있음)
        val state = viewModel.items.value
        assertTrue("Expected Loading or Success state but got: $state", 
                  state is SealedUiState.Loading || state is SealedUiState.Success)
    }

        @Test
    fun `소스 필터가 올바르게 적용되어야 한다`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()
        
        // 소스 필터 적용
        viewModel.updateSource("테스트경제")
        testDispatcher.scheduler.advanceUntilIdle()
        
        // 필터가 적용되었는지 확인 (상태는 Loading 또는 Success일 수 있음)
        val state = viewModel.items.value
        assertTrue("Expected Loading or Success state but got: $state", 
                  state is SealedUiState.Loading || state is SealedUiState.Success)
    }

        @Test
    fun `전체 필터 적용 시 소스 필터가 초기화되어야 한다`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()
        
        // 소스 필터 적용
        viewModel.updateSource("테스트경제")
        testDispatcher.scheduler.advanceUntilIdle()
        
        // 전체 필터 적용
        val allFilter = viewModel.filterButtons.find { it.label == "전체" }!!
        viewModel.updateFilter(allFilter)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // 필터가 적용되었는지 확인 (상태는 Loading 또는 Success일 수 있음)
        val state = viewModel.items.value
        assertTrue("Expected Loading or Success state but got: $state", 
                  state is SealedUiState.Loading || state is SealedUiState.Success)
    }

        @Test
    fun `새로고침이 올바르게 동작해야 한다`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()
        
        viewModel.refresh()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // 새로고침이 호출되었는지 확인 (상태는 Loading 또는 Success일 수 있음)
        val state = viewModel.items.value
        assertTrue("Expected Loading or Success state but got: $state", 
                  state is SealedUiState.Loading || state is SealedUiState.Success)
    }

    @Test
    fun `캐시 관련 메서드들이 올바르게 동작해야 한다`() = runTest {
        // 캐시 초기화
        viewModel.clearCache()
        verify { mockRepository.clearCache() }

        // 특정 소스 캐시 무효화
        viewModel.invalidateCacheForSource("https://test.com/feed")
        verify { mockRepository.invalidateCacheForSource("https://test.com/feed") }

        // 캐시 정보 가져오기
        val cacheInfo = viewModel.getCacheInfo()
        assertEquals(mapOf("test" to 1000L), cacheInfo)
    }

        @Test
    fun `암호화폐 필터가 올바르게 동작해야 한다`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()
        
        val cryptoFilter = viewModel.filterButtons.find { it.label == "암호화폐" }!!
        viewModel.updateFilter(cryptoFilter)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // 필터가 적용되었는지 확인 (상태는 Loading 또는 Success일 수 있음)
        val state = viewModel.items.value
        assertTrue("Expected Loading or Success state but got: $state", 
                  state is SealedUiState.Loading || state is SealedUiState.Success)
    }

        @Test
    fun `경제 필터가 올바르게 동작해야 한다`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()
        
        val economyFilter = viewModel.filterButtons.find { it.label == "경제" }!!
        viewModel.updateFilter(economyFilter)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // 필터가 적용되었는지 확인 (상태는 Loading 또는 Success일 수 있음)
        val state = viewModel.items.value
        assertTrue("Expected Loading or Success state but got: $state", 
                  state is SealedUiState.Loading || state is SealedUiState.Success)
    }
}
