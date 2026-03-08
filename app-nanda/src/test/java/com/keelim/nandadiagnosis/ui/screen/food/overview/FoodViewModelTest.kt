package com.keelim.nandadiagnosis.ui.screen.food.overview

import app.cash.turbine.test
import com.keelim.shared.data.database.dao.FoodDao
import com.keelim.shared.data.database.model.FoodEntity
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalCoroutinesApi::class)
class FoodViewModelTest : FunSpec({
    lateinit var foodDao: FoodDao
    lateinit var todayFoodsFlow: MutableStateFlow<List<FoodEntity>>
    lateinit var totalCaloriesFlow: MutableStateFlow<Int?>
    lateinit var viewModel: FoodViewModel
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)
    val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

    extension(mainDispatcherRule)

    beforeTest {
        foodDao = mockk(relaxed = true)
        todayFoodsFlow = MutableStateFlow(emptyList())
        totalCaloriesFlow = MutableStateFlow(null)
        every { foodDao.getByDate(any()) } returns todayFoodsFlow
        every { foodDao.getTotalCaloriesByDate(any()) } returns totalCaloriesFlow
        viewModel = FoodViewModel(foodDao)
    }

    test("todayFoods exposes dao records") {
        runTest(testDispatcher) {
            val record = FoodEntity(
                id = 1L,
                title = "샐러드",
                calories = 120,
                date = today,
                time = 10L,
            )
            todayFoodsFlow.value = listOf(record)

            viewModel.todayFoods.test {
                awaitItem() shouldBe emptyList()
                advanceUntilIdle()

                awaitItem() shouldBe listOf(record)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    test("todayTotalCalories maps null to zero and emits dao totals") {
        runTest(testDispatcher) {
            viewModel.todayTotalCalories.test {
                awaitItem() shouldBe 0

                totalCaloriesFlow.value = 450
                advanceUntilIdle()

                awaitItem() shouldBe 450
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    test("addFood inserts parsed calories when inputs are valid") {
        runTest(testDispatcher) {
            coEvery { foodDao.insert(any()) } returns Unit

            viewModel.addFood(title = "비빔밥", calories = "550")
            advanceUntilIdle()

            coVerify {
                foodDao.insert(
                    match {
                        it.title == "비빔밥" &&
                            it.calories == 550 &&
                            it.date == today &&
                            it.time > 0L
                    },
                )
            }
        }
    }

    test("addFood ignores invalid calories or blank title") {
        runTest(testDispatcher) {
            viewModel.addFood(title = "주스", calories = "abc")
            viewModel.addFood(title = "", calories = "100")
            advanceUntilIdle()

            coVerify(exactly = 0) { foodDao.insert(any()) }
        }
    }

    test("deleteFood removes the record by id") {
        runTest(testDispatcher) {
            coEvery { foodDao.deleteById(9L) } returns Unit

            viewModel.deleteFood(9L)
            advanceUntilIdle()

            coVerify { foodDao.deleteById(9L) }
        }
    }
})
