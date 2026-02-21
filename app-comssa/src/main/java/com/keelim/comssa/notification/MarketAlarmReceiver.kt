package com.keelim.comssa.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MarketAlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationManager: MarketNotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != MarketNotificationManager.ACTION_SHOW_NOTIFICATION) {
            return
        }

        val scheduleId = intent.getStringExtra(MarketNotificationManager.EXTRA_SCHEDULE_ID) ?: return
        val scheduleName = intent.getStringExtra(MarketNotificationManager.EXTRA_SCHEDULE_NAME)

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                notificationManager.showNotification(scheduleId, scheduleName)
            } finally {
                pendingResult.finish()
            }
        }
    }
}
