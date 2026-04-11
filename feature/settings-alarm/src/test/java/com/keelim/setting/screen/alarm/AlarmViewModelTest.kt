package com.keelim.setting.screen.alarm

import app.cash.turbine.test
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.data.repository.AlarmRepository
import com.keelim.model.Alarm
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class AlarmViewModelTest : FunSpec({
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)
    val sampleAlarm = Alarm(
        title = "Wake up",
        subTitle = "Morning routine",
        receiveDate = "2026-03-08T06:00:00Z",
    )

    extension(mainDispatcherRule)

    fun createViewModel(alarms: Flow<List<Alarm>>): AlarmViewModel {
        val repository = mockk<AlarmRepository>()
        every { repository.getAlarms() } returns alarms
        return AlarmViewModel(repository)
    }

    test("screenState starts in loading state") {
        val viewModel = createViewModel(MutableStateFlow(emptyList()))

        viewModel.screenState.value shouldBe SealedUiState.Loading
    }

    test("repository alarms emit success state") {
        runTest(testDispatcher) {
            val viewModel = createViewModel(MutableStateFlow(listOf(sampleAlarm)))

            viewModel.screenState.test {
                awaitItem() shouldBe SealedUiState.Loading
                advanceUntilIdle()
                awaitItem() shouldBe SealedUiState.Success(listOf(sampleAlarm))
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    test("empty repository output keeps the loading state stable") {
        runTest(testDispatcher) {
            val viewModel = createViewModel(MutableStateFlow(emptyList()))

            viewModel.screenState.test {
                awaitItem() shouldBe SealedUiState.Loading
                advanceUntilIdle()

                viewModel.screenState.value shouldBe SealedUiState.Loading
                expectNoEvents()
            }
        }
    }
})
