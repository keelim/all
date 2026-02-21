package com.keelim.nandadiagnosis.ui.screen.length

import app.cash.turbine.test
import com.keelim.data.repository.LengthRepository
import com.keelim.model.LengthRecord
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class LengthViewModelTest : FunSpec({
    lateinit var mockRepo: LengthRepository
    lateinit var viewModel: LengthViewModel

    beforeTest {
        mockRepo = mockk(relaxed = true)
        viewModel = LengthViewModel(mockRepo)
    }

    test("기록_추가_시_리스트에_반영된다") {
        runTest {
            val record = LengthRecord(date = "2024-06-01", length = 12.3f)
            val flow = MutableStateFlow(emptyList<LengthRecord>())
            every { mockRepo.getAllRecords() } returns flow
            coEvery { mockRepo.addRecord(record) } answers { flow.value = flow.value + record }

            viewModel.addRecord(record)
            viewModel.fetchRecords()

            viewModel.records.test {
                val result = awaitItem()
                result shouldBe listOf(record)
                cancelAndIgnoreRemainingEvents()
            }
            coVerify { mockRepo.addRecord(record) }
        }
    }

    test("기록_삭제_시_리스트에서_사라진다") {
        runTest {
            val record1 = LengthRecord(date = "2024-06-01", length = 12.3f)
            val record2 = LengthRecord(date = "2024-06-02", length = 13.0f)
            val flow = MutableStateFlow(listOf(record1, record2))
            every { mockRepo.getAllRecords() } returns flow
            coEvery { mockRepo.deleteRecord("2024-06-01") } answers { flow.value = flow.value.filterNot { it.date == "2024-06-01" } }

            viewModel.deleteRecord("2024-06-01")
            viewModel.fetchRecords()

            viewModel.records.test {
                val result = awaitItem()
                result shouldBe listOf(record2)
                cancelAndIgnoreRemainingEvents()
            }
            coVerify { mockRepo.deleteRecord("2024-06-01") }
        }
    }

    test("빈_상태에서_fetchRecords_호출_시_빈_리스트_반환") {
        runTest {
            every { mockRepo.getAllRecords() } returns MutableStateFlow(emptyList())
            viewModel.fetchRecords()

            viewModel.records.test {
                val result = awaitItem()
                result shouldBe emptyList<LengthRecord>()
                cancelAndIgnoreRemainingEvents()
            }
        }
    }
})
