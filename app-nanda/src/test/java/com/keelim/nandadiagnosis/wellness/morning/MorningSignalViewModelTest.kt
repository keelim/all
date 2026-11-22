package com.keelim.nandadiagnosis.wellness.morning

import com.keelim.common.platform.notification.NotificationRepeat
import com.keelim.common.platform.notification.NotificationScheduleResult
import com.keelim.testing.platform.FakeNotificationScheduler
import com.keelim.testing.platform.FakeTimeProvider
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.time.Instant
import java.time.ZoneId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class MorningSignalViewModelTest : FunSpec({
    val dispatcher = UnconfinedTestDispatcher()

    beforeTest { kotlinx.coroutines.Dispatchers.setMain(dispatcher) }
    afterTest { kotlinx.coroutines.Dispatchers.resetMain() }

    test("defaults to disabled at 07:00") {
        val viewModel = createViewModel()

        viewModel.uiState.value.settings shouldBe MorningSignalSettings()
    }

    test("schedules today when the selected time has not passed") {
        runTest {
            val scheduler = FakeNotificationScheduler()
            val viewModel = createViewModel(
                scheduler = scheduler,
                initialInstant = Instant.parse("2026-08-02T21:30:00Z"),
            )

            viewModel.setEnabled(true)
            advanceUntilIdle()

            val request = scheduler.scheduledRequests.single()
            request.scheduledAt shouldBe Instant.parse("2026-08-02T22:00:00Z")
            request.repeat shouldBe NotificationRepeat.DAILY
            viewModel.uiState.value.settings.enabled shouldBe true
        }
    }

    test("schedules the next local day after the selected time") {
        runTest {
            val scheduler = FakeNotificationScheduler()
            val viewModel = createViewModel(
                scheduler = scheduler,
                initialInstant = Instant.parse("2026-08-02T22:30:00Z"),
            )

            viewModel.setEnabled(true)
            advanceUntilIdle()

            scheduler.scheduledRequests.single().scheduledAt shouldBe
                Instant.parse("2026-08-03T22:00:00Z")
        }
    }

    test("changing time replaces the scheduled request and disabling cancels it") {
        runTest {
            val scheduler = FakeNotificationScheduler()
            val viewModel = createViewModel(scheduler = scheduler)

            viewModel.setEnabled(true)
            viewModel.setTime(hour = 8, minute = 15)
            advanceUntilIdle()

            scheduler.scheduledRequests.single().scheduledAt shouldBe
                Instant.parse("2026-08-02T23:15:00Z")
            viewModel.uiState.value.settings.hour shouldBe 8
            viewModel.uiState.value.settings.minute shouldBe 15

            viewModel.setEnabled(false)
            advanceUntilIdle()

            scheduler.cancelledIds shouldBe listOf("morning-signal")
            viewModel.uiState.value.settings.enabled shouldBe false
        }
    }

    test("permission denial leaves the signal disabled") {
        runTest {
            val scheduler = FakeNotificationScheduler().apply {
                scheduleResult = NotificationScheduleResult.PermissionDenied
            }
            val viewModel = createViewModel(scheduler = scheduler)

            viewModel.setEnabled(true)
            advanceUntilIdle()

            viewModel.uiState.value.settings.enabled shouldBe false
            viewModel.uiState.value.message shouldBe
                MorningSignalMessage.NOTIFICATION_PERMISSION_DENIED
        }
    }
})

private fun createViewModel(
    scheduler: FakeNotificationScheduler = FakeNotificationScheduler(),
    initialInstant: Instant = Instant.parse("2026-08-02T20:00:00Z"),
    initialSettings: MorningSignalSettings = MorningSignalSettings(),
): MorningSignalViewModel = MorningSignalViewModel(
    settingsStore = FakeMorningSignalSettingsStore(initialSettings),
    notificationScheduler = scheduler,
    timeProvider = FakeTimeProvider(initialInstant, ZoneId.of("Asia/Seoul")),
    notificationContent = MorningSignalNotificationContent(
        title = "Morning signal",
        body = "Check in",
        channelName = "Morning signal",
        channelDescription = "Morning check-in reminders",
    ),
)

private class FakeMorningSignalSettingsStore(
    initialSettings: MorningSignalSettings,
) : MorningSignalSettingsStore {
    private var settings = initialSettings

    override fun load(): MorningSignalSettings = settings

    override fun save(settings: MorningSignalSettings) {
        this.settings = settings
    }
}
