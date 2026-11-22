package com.keelim.commonAndroid.platform.notification

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.keelim.common.platform.notification.NotificationChannelKey
import com.keelim.common.platform.notification.NotificationContent
import com.keelim.common.platform.notification.NotificationRepeat
import com.keelim.common.platform.notification.NotificationRequest
import com.keelim.common.platform.notification.NotificationScheduleResult
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.mockk.verify
import java.time.Instant

class WorkManagerNotificationSchedulerTest : FunSpec({
    test("permission denial does not enqueue work") {
        val scheduler = WorkManagerNotificationScheduler(
            context = mockk<Context>(relaxed = true),
            workManager = mockk<WorkManager>(relaxed = true),
            canPostNotifications = { false },
        )

        scheduler.schedule(
            NotificationRequest(
                id = "plan",
                scheduledAt = Instant.EPOCH,
                channel = NotificationChannelKey("plan", "Plan", "Plan reminders"),
                content = NotificationContent("Plan", "Open the app"),
            ),
        ) shouldBe NotificationScheduleResult.PermissionDenied
    }

    test("daily schedules enqueue periodic work") {
        val workManager = mockk<WorkManager>(relaxed = true)
        val scheduler = WorkManagerNotificationScheduler(
            context = mockk<Context>(relaxed = true),
            workManager = workManager,
            now = { Instant.parse("2026-08-02T20:00:00Z") },
            canPostNotifications = { true },
        )

        scheduler.schedule(
            NotificationRequest(
                id = "morning",
                scheduledAt = Instant.parse("2026-08-02T22:00:00Z"),
                channel = NotificationChannelKey("morning", "Morning", "Morning reminders"),
                content = NotificationContent("Morning", "Check in"),
                repeat = NotificationRepeat.DAILY,
            ),
        ) shouldBe NotificationScheduleResult.Scheduled

        verify(exactly = 1) {
            workManager.enqueueUniquePeriodicWork(
                "keelim.platform.notification.morning",
                ExistingPeriodicWorkPolicy.REPLACE,
                any<PeriodicWorkRequest>(),
            )
        }
    }

    test("one-time schedules still enqueue one-time work") {
        val workManager = mockk<WorkManager>(relaxed = true)
        val scheduler = WorkManagerNotificationScheduler(
            context = mockk<Context>(relaxed = true),
            workManager = workManager,
            canPostNotifications = { true },
        )

        scheduler.schedule(
            NotificationRequest(
                id = "one-time",
                scheduledAt = Instant.EPOCH,
                channel = NotificationChannelKey("one-time", "One time", "One-time reminders"),
                content = NotificationContent("One time", "Open the app"),
            ),
        ) shouldBe NotificationScheduleResult.Scheduled

        verify(exactly = 1) {
            workManager.enqueueUniqueWork(
                "keelim.platform.notification.one-time",
                ExistingWorkPolicy.REPLACE,
                any<OneTimeWorkRequest>(),
            )
        }
    }

    test("cancel removes unique work and tagged work") {
        val workManager = mockk<WorkManager>(relaxed = true)
        val scheduler = WorkManagerNotificationScheduler(
            context = mockk<Context>(relaxed = true),
            workManager = workManager,
            canPostNotifications = { true },
        )

        scheduler.cancel("morning")
        scheduler.cancelByTag("morning-signal")

        verify(exactly = 1) {
            workManager.cancelUniqueWork("keelim.platform.notification.morning")
            workManager.cancelAllWorkByTag("morning-signal")
        }
    }
})
