package com.keelim.nandadiagnosis.ui.screen.length

import app.cash.turbine.test
import com.keelim.data.repository.LengthRepository
import com.keelim.model.LengthRecord
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LengthViewModelTest {
    private lateinit var mockRepo: LengthRepository
    private lateinit var viewModel: LengthViewModel

    @Before
    fun setup() {
        mockRepo = mockk(relaxed = true)
        viewModel = LengthViewModel(mockRepo)
    }

    @Test
    fun 기록_추가_시_리스트에_반영된다() = runTest {
        val record = LengthRecord(date = "2024-06-01", length = 12.3f)
        val flow = MutableStateFlow(emptyList<LengthRecord>())
        every { mockRepo.getAllRecords() } returns flow
        coEvery { mockRepo.addRecord(record) } answers { flow.value = flow.value + record }

        viewModel.addRecord(record)
        viewModel.fetchRecords()

        viewModel.records.test {
            assertEquals(listOf<LengthRecord>(), awaitItem())
            val result = awaitItem()
            assertEquals(listOf(record), result)
            cancelAndIgnoreRemainingEvents()
        }
        coVerify { mockRepo.addRecord(record) }
    }

    @Test
    fun 기록_삭제_시_리스트에서_사라진다() = runTest {
        val record1 = LengthRecord(date = "2024-06-01", length = 12.3f)
        val record2 = LengthRecord(date = "2024-06-02", length = 13.0f)
        val flow = MutableStateFlow(listOf(record1, record2))
        every { mockRepo.getAllRecords() } returns flow
        coEvery { mockRepo.deleteRecord("2024-06-01") } answers { flow.value = flow.value.filterNot { it.date == "2024-06-01" } }

        viewModel.deleteRecord("2024-06-01")
        viewModel.fetchRecords()

        viewModel.records.test {
            val result = awaitItem()
            assertEquals(listOf(record2), result)
            cancelAndIgnoreRemainingEvents()
        }
        coVerify { mockRepo.deleteRecord("2024-06-01") }
    }

    @Test
    fun 빈_상태에서_fetchRecords_호출_시_빈_리스트_반환() = runTest {
        every { mockRepo.getAllRecords() } returns MutableStateFlow(emptyList())
        viewModel.fetchRecords()

        viewModel.records.test {
            val result = awaitItem()
            assertEquals(emptyList<LengthRecord>(), result)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
