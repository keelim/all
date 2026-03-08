package com.keelim.nandadiagnosis.ui.screen.water

import app.cash.turbine.test
import com.keelim.common.extensions.formatUiTime
import com.keelim.model.DailyWaterTotal
import com.keelim.shared.data.database.dao.WaterIntakeDao
import com.keelim.shared.data.database.model.WaterIntake
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalCoroutinesApi::class)
class WaterIntakeViewModelTest : FunSpec({
    lateinit var waterIntakeDao: WaterIntakeDao
    lateinit var totalFlow: MutableStateFlow<Int?>
    lateinit var recordsFlow: MutableStateFlow<List<WaterIntake>>
    lateinit var weeklyHistoryFlow: MutableStateFlow<List<DailyWaterTotal>>
    lateinit var viewModel: WaterIntakeViewModel
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)
    val today = LocalDate.now(ZoneId.systemDefault()).toString()

    extension(mainDispatcherRule)

    beforeTest {
        waterIntakeDao = mockk(relaxed = true)
        totalFlow = MutableStateFlow(null)
        recordsFlow = MutableStateFlow(emptyList())
        weeklyHistoryFlow = MutableStateFlow(emptyList())
        every { waterIntakeDao.getTotalByDate(any()) } returns totalFlow
        every { waterIntakeDao.getByDate(any()) } returns recordsFlow
        every { waterIntakeDao.getDailyTotals(7) } returns weeklyHistoryFlow
        viewModel = WaterIntakeViewModel(waterIntakeDao)
    }

    test("dailyGoal starts at 2000 and can be updated") {
        viewModel.dailyGoal.value shouldBe 2000

        viewModel.setDailyGoal(2500)

        viewModel.dailyGoal.value shouldBe 2500
    }

    test("todayTotal maps null to zero and emits totals from dao") {
        runTest(testDispatcher) {
            viewModel.todayTotal.test {
                awaitItem() shouldBe 0

                totalFlow.value = 900
                advanceUntilIdle()

                awaitItem() shouldBe 900
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    test("todayRecords maps dao records to ui models") {
        runTest(testDispatcher) {
            val timestamp = 1_700_000_000_000L
            recordsFlow.value = listOf(
                WaterIntake(
                    id = 3L,
                    amount = 250,
                    timestamp = timestamp,
                    date = today,
                ),
            )
            val localDateTime = Instant.ofEpochMilli(timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
            val expectedTime = formatUiTime(localDateTime.hour, localDateTime.minute)

            viewModel.todayRecords.test {
                awaitItem() shouldBe emptyList()
                advanceUntilIdle()

                awaitItem() shouldBe listOf(
                    WaterIntakeUiModel(
                        id = 3L,
                        amount = 250,
                        formattedTime = expectedTime,
                    ),
                )
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    test("weeklyHistory exposes dao totals") {
        runTest(testDispatcher) {
            val history = listOf(DailyWaterTotal(date = "2024-01-01", totalAmount = 1200))
            weeklyHistoryFlow.value = history

            viewModel.weeklyHistory.test {
                awaitItem() shouldBe emptyList()
                advanceUntilIdle()

                awaitItem() shouldBe history
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    test("addWaterIntake inserts a record for today") {
        runTest(testDispatcher) {
            coEvery { waterIntakeDao.insert(any()) } returns Unit

            viewModel.addWaterIntake(500)
            advanceUntilIdle()

            coVerify {
                waterIntakeDao.insert(
                    match {
                        it.amount == 500 &&
                            it.date == today &&
                            it.timestamp > 0L
                    },
                )
            }
        }
    }

    test("deleteWaterIntake removes a record by id") {
        runTest(testDispatcher) {
            coEvery { waterIntakeDao.deleteById(11L) } returns Unit

            viewModel.deleteWaterIntake(11L)
            advanceUntilIdle()

            coVerify { waterIntakeDao.deleteById(11L) }
        }
    }
})
