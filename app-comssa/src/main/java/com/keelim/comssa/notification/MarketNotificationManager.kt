package com.keelim.comssa.notification

import android.Manifest
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.keelim.data.model.MarketSchedule
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.util.Calendar
import jakarta.inject.Inject
import javax.inject.Singleton

@Singleton
class MarketNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for stock market open times"
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun scheduleNotification(schedule: MarketSchedule) {
        if (!schedule.isEnabled) {
            cancelNotification(schedule)
            return
        }

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, schedule.hour)
            set(Calendar.MINUTE, schedule.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // If time has passed today, schedule for tomorrow
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val intent = Intent(context, MarketAlarmReceiver::class.java).apply {
            action = ACTION_SHOW_NOTIFICATION
            putExtra(EXTRA_SCHEDULE_ID, schedule.id)
            putExtra(EXTRA_SCHEDULE_NAME, schedule.name)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            schedule.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                } else {
                    // Fallback to inexact alarm
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
            Timber.d("Scheduled notification for ${schedule.name} at ${calendar.time}")
        } catch (e: SecurityException) {
            Timber.e(e, "Failed to schedule exact alarm")
        }
    }

    fun cancelNotification(schedule: MarketSchedule) {
        val intent = Intent(context, MarketAlarmReceiver::class.java).apply {
            action = ACTION_SHOW_NOTIFICATION
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            schedule.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
        Timber.d("Cancelled notification for ${schedule.name}")
    }

    fun showNotification(scheduleId: String, scheduleName: String) {
        val notificationId = scheduleId.hashCode()

        val notification = if (Build.VERSION.SDK_INT >= 36) {
            // Android 16+ with ProgressStyle for Live Updates
            createLiveNotification(scheduleName)
        } else {
            // Standard notification for older versions
            createStandardNotification(scheduleName)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notificationManager.notify(notificationId, notification)
            }
        } else {
            notificationManager.notify(notificationId, notification)
        }
    }

    private fun createStandardNotification(scheduleName: String): Notification {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            launchIntent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("📈 Market Opening!")
            .setContentText("$scheduleName is now open for trading")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()
    }

    @Suppress("NewApi")
    private fun createLiveNotification(scheduleName: String): Notification {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            launchIntent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        // Android 16+ Live Update with ProgressStyle
        val style = Notification.ProgressStyle()
            .addProgressPoint(Notification.ProgressStyle.Point(0))
            .addProgressPoint(Notification.ProgressStyle.Point(100))

        return Notification.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("📈 $scheduleName")
            .setContentText("Market is now open for trading!")
            .setStyle(style)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
    }

    companion object {
        const val CHANNEL_ID = "market_notification_channel"
        const val CHANNEL_NAME = "Stock Market Notifications"
        const val ACTION_SHOW_NOTIFICATION = "com.keelim.comssa.ACTION_SHOW_MARKET_NOTIFICATION"
        const val EXTRA_SCHEDULE_ID = "schedule_id"
        const val EXTRA_SCHEDULE_NAME = "schedule_name"
    }
}
