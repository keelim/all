package com.keelim.comssa.ui.screen.notification

import app.cash.turbine.test
import com.keelim.comssa.notification.MarketNotificationManager
import com.keelim.data.model.MarketSchedule
import com.keelim.data.repository.MarketNotificationRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class MarketNotificationViewModelTest {
    private lateinit var viewModel: MarketNotificationViewModel
    private lateinit var repository: MarketNotificationRepository
    private lateinit var notificationManager: MarketNotificationManager
    private val testDispatcher = StandardTestDispatcher()

    private val testSchedule = MarketSchedule(
        id = "test-1",
        name = "Test Market",
        hour = 9,
        minute = 0,
        isEnabled = true,
        isDefault = false
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        notificationManager = mockk(relaxed = true)

        coEvery { repository.getSchedules() } returns flowOf(listOf(testSchedule))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `schedules flow emits list from repository`() = runTest {
        viewModel = MarketNotificationViewModel(repository, notificationManager)
        advanceUntilIdle()

        viewModel.schedules.test {
            val schedules = awaitItem()
            assertEquals(1, schedules.size)
            assertEquals("Test Market", schedules[0].name)
        }
    }

    @Test
    fun `toggleSchedule enables schedule and schedules notification`() = runTest {
        val disabledSchedule = testSchedule.copy(isEnabled = false)
        coEvery { repository.getSchedules() } returns flowOf(listOf(disabledSchedule))
        coEvery { repository.updateSchedule(any()) } returns Unit

        viewModel = MarketNotificationViewModel(repository, notificationManager)
        advanceUntilIdle()

        viewModel.toggleSchedule(disabledSchedule)
        advanceUntilIdle()

        coVerify {
            repository.updateSchedule(match { it.isEnabled })
            notificationManager.scheduleNotification(match { it.isEnabled })
        }
    }

    @Test
    fun `toggleSchedule disables schedule and cancels notification`() = runTest {
        coEvery { repository.updateSchedule(any()) } returns Unit

        viewModel = MarketNotificationViewModel(repository, notificationManager)
        advanceUntilIdle()

        viewModel.toggleSchedule(testSchedule)
        advanceUntilIdle()

        coVerify {
            repository.updateSchedule(match { !it.isEnabled })
            notificationManager.cancelNotification(match { !it.isEnabled })
        }
    }

    @Test
    fun `addCustomSchedule creates and schedules new notification`() = runTest {
        coEvery { repository.addSchedule(any()) } returns Unit

        viewModel = MarketNotificationViewModel(repository, notificationManager)
        advanceUntilIdle()

        viewModel.addCustomSchedule("Custom Market", 10, 30)
        advanceUntilIdle()

        coVerify {
            repository.addSchedule(match {
                it.name == "Custom Market" &&
                it.hour == 10 &&
                it.minute == 30 &&
                it.isEnabled &&
                !it.isDefault
            })
            notificationManager.scheduleNotification(any())
        }
    }

    @Test
    fun `removeSchedule cancels notification and removes from repository`() = runTest {
        coEvery { repository.removeSchedule(any()) } returns Unit

        viewModel = MarketNotificationViewModel(repository, notificationManager)
        advanceUntilIdle()

        viewModel.removeSchedule(testSchedule)
        advanceUntilIdle()

        coVerify {
            notificationManager.cancelNotification(testSchedule)
            repository.removeSchedule("test-1")
        }
    }

    @Test
    fun `showTimePicker sets showTimePicker to true`() = runTest {
        viewModel = MarketNotificationViewModel(repository, notificationManager)
        advanceUntilIdle()

        viewModel.showTimePicker()

        viewModel.showTimePicker.test {
            assertTrue(awaitItem())
        }
    }

    @Test
    fun `hideTimePicker sets showTimePicker to false`() = runTest {
        viewModel = MarketNotificationViewModel(repository, notificationManager)
        advanceUntilIdle()

        viewModel.showTimePicker()
        viewModel.hideTimePicker()

        viewModel.showTimePicker.test {
            assertFalse(awaitItem())
        }
    }

    @Test
    fun `rescheduleAllEnabled schedules all enabled notifications`() = runTest {
        val enabledSchedule1 = testSchedule.copy(id = "enabled-1", isEnabled = true)
        val enabledSchedule2 = testSchedule.copy(id = "enabled-2", isEnabled = true)
        val disabledSchedule = testSchedule.copy(id = "disabled-1", isEnabled = false)

        coEvery { repository.getSchedules() } returns flowOf(
            listOf(enabledSchedule1, enabledSchedule2, disabledSchedule)
        )

        viewModel = MarketNotificationViewModel(repository, notificationManager)
        advanceUntilIdle()

        viewModel.rescheduleAllEnabled()
        advanceUntilIdle()

        coVerify(exactly = 2) {
            notificationManager.scheduleNotification(match { it.isEnabled })
        }
    }
}
