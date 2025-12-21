package com.keelim.nandadiagnosis.notification

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
import com.keelim.data.model.Medication
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MedicationNotificationManager @Inject constructor(
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
                description = "Notifications for medication reminders"
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun scheduleNotification(medication: Medication) {
        if (!medication.isEnabled) {
            cancelNotification(medication)
            return
        }

        val now = ZonedDateTime.now(ZoneId.systemDefault())
        var scheduledTime = now
            .withHour(medication.hour)
            .withMinute(medication.minute)
            .withSecond(0)
            .withNano(0)

        // If time has passed today, schedule for tomorrow
        if (scheduledTime.isBefore(now) || scheduledTime.isEqual(now)) {
            scheduledTime = scheduledTime.plusDays(1)
        }

        val triggerAtMillis = scheduledTime.toInstant().toEpochMilli()

        val intent = Intent(context, MedicationAlarmReceiver::class.java).apply {
            action = ACTION_SHOW_NOTIFICATION
            putExtra(EXTRA_MEDICATION_ID, medication.id)
            putExtra(EXTRA_MEDICATION_NAME, medication.name)
            putExtra(EXTRA_MEDICATION_DOSAGE, medication.dosage)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            medication.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            // Use setAndAllowWhileIdle for compatibility without SCHEDULE_EXACT_ALARM permission
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
            Timber.d("Scheduled notification for ${medication.name} at $scheduledTime")
        } catch (e: SecurityException) {
            Timber.e(e, "Failed to schedule alarm")
        }
    }

    fun cancelNotification(medication: Medication) {
        val intent = Intent(context, MedicationAlarmReceiver::class.java).apply {
            action = ACTION_SHOW_NOTIFICATION
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            medication.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
        Timber.d("Cancelled notification for ${medication.name}")
    }

    fun showNotification(medicationId: String, medicationName: String, medicationDosage: String) {
        val notificationId = medicationId.hashCode()

        val notification = if (Build.VERSION.SDK_INT >= 36) {
            createLiveNotification(medicationName, medicationDosage)
        } else {
            createStandardNotification(medicationName, medicationDosage)
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

    private fun createStandardNotification(medicationName: String, medicationDosage: String): Notification {
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
            .setContentTitle("💊 복약 시간!")
            .setContentText("$medicationName ($medicationDosage) 복용 시간입니다")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()
    }

    @Suppress("NewApi")
    private fun createLiveNotification(medicationName: String, medicationDosage: String): Notification {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            launchIntent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val style = Notification.ProgressStyle()
            .addProgressPoint(Notification.ProgressStyle.Point(0))
            .addProgressPoint(Notification.ProgressStyle.Point(100))

        return Notification.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("💊 $medicationName")
            .setContentText("$medicationDosage 복용 시간입니다!")
            .setStyle(style)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
    }

    companion object {
        const val CHANNEL_ID = "medication_notification_channel"
        const val CHANNEL_NAME = "Medication Reminders"
        const val ACTION_SHOW_NOTIFICATION = "com.keelim.nandadiagnosis.ACTION_SHOW_MEDICATION_NOTIFICATION"
        const val EXTRA_MEDICATION_ID = "medication_id"
        const val EXTRA_MEDICATION_NAME = "medication_name"
        const val EXTRA_MEDICATION_DOSAGE = "medication_dosage"
    }
}
