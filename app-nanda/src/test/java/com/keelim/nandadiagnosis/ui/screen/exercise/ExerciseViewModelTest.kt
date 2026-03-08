package com.keelim.nandadiagnosis.ui.screen.exercise

import app.cash.turbine.test
import com.keelim.shared.data.database.dao.ExerciseDao
import com.keelim.shared.data.database.model.ExerciseEntity
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
class ExerciseViewModelTest : FunSpec({
    lateinit var exerciseDao: ExerciseDao
    lateinit var todayExercisesFlow: MutableStateFlow<List<ExerciseEntity>>
    lateinit var viewModel: ExerciseViewModel
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)
    val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

    extension(mainDispatcherRule)

    beforeTest {
        exerciseDao = mockk(relaxed = true)
        todayExercisesFlow = MutableStateFlow(emptyList())
        every { exerciseDao.getByDate(any()) } returns todayExercisesFlow
        viewModel = ExerciseViewModel(exerciseDao)
    }

    test("todayExercises exposes dao records") {
        runTest(testDispatcher) {
            val record = ExerciseEntity(
                id = 1L,
                title = "걷기",
                duration = "30분",
                date = today,
                time = 10L,
            )
            todayExercisesFlow.value = listOf(record)

            viewModel.todayExercises.test {
                awaitItem() shouldBe emptyList()
                advanceUntilIdle()

                awaitItem() shouldBe listOf(record)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    test("addExercise inserts a record when inputs are valid") {
        runTest(testDispatcher) {
            coEvery { exerciseDao.insert(any()) } returns Unit

            viewModel.addExercise(title = "자전거", duration = "45분")
            advanceUntilIdle()

            coVerify {
                exerciseDao.insert(
                    match {
                        it.title == "자전거" &&
                            it.duration == "45분" &&
                            it.date == today &&
                            it.time > 0L
                    },
                )
            }
        }
    }

    test("addExercise ignores blank title or duration") {
        runTest(testDispatcher) {
            viewModel.addExercise(title = "", duration = "20분")
            viewModel.addExercise(title = "산책", duration = "")
            advanceUntilIdle()

            coVerify(exactly = 0) { exerciseDao.insert(any()) }
        }
    }

    test("deleteExercise removes the record by id") {
        runTest(testDispatcher) {
            coEvery { exerciseDao.deleteById(7L) } returns Unit

            viewModel.deleteExercise(7L)
            advanceUntilIdle()

            coVerify { exerciseDao.deleteById(7L) }
        }
    }
})
