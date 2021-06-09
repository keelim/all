package com.keelim.cnubus.widget


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import com.keelim.cnubus.R

class WidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        ContextCompat.startForegroundService(
            context!!,
            Intent(context, UpdateWidgetServices::class.java)
        )
    }

    class UpdateWidgetServices : LifecycleService() {
        override fun onCreate() {
            super.onCreate()
            // 포그라운드 서비스를 만든다.
            createChannelIfNeed()
            startForeground(
                NOTIFICATION_ID,
                createNotification()
            )
        }

        override fun onDestroy() {
            super.onDestroy()
            stopForeground(true)
        }

        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            val updateViews = RemoteViews(packageName,R.layout.widget_simple).apply{
                setTextViewText(
                    R.id.widget_result_text,
                    "권한 없음"
                )
            }
            updateWidget(updateViews)
            stopSelf()

            return super.onStartCommand(intent, flags, startId)
        }

        private fun createChannelIfNeed() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                (getSystemService(NOTIFICATION_SERVICE) as? NotificationManager)
                    ?.createNotificationChannel(
                        NotificationChannel(
                            WIDGET_REFRESH_CHANNEL_ID,
                            "위젯 갱신 채널",
                            NotificationManager.IMPORTANCE_LOW
                        )
                    )
            }
        }
        private fun createNotification(): Notification =
            NotificationCompat.Builder(this, WIDGET_REFRESH_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build()

        private fun updateWidget(updateViews:RemoteViews){
            val widgetProvider = ComponentName(this, WidgetProvider::class.java)
            AppWidgetManager.getInstance(this).updateAppWidget(widgetProvider, updateViews)
        }
    }

    companion object {
        private const val WIDGET_REFRESH_CHANNEL_ID = "WIDGET_REFRESH_CHANNEL_ID"
        private const val NOTIFICATION_ID = 101
    }

}