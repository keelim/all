package com.keelim.commonAndroid.platform.notification

import android.content.Context
import androidx.work.WorkManager
import com.keelim.common.platform.notification.NotificationChannelKey
import com.keelim.common.platform.notification.NotificationContent
import com.keelim.common.platform.notification.NotificationRequest
import com.keelim.common.platform.notification.NotificationScheduleResult
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
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
})
