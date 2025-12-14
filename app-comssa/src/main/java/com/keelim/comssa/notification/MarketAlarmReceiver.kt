package com.keelim.comssa.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MarketAlarmReceiver : BroadcastReceiver() {
    
    @Inject
    lateinit var notificationManager: MarketNotificationManager
    
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == MarketNotificationManager.ACTION_SHOW_NOTIFICATION) {
            val scheduleId = intent.getStringExtra(MarketNotificationManager.EXTRA_SCHEDULE_ID) ?: return
            val scheduleName = intent.getStringExtra(MarketNotificationManager.EXTRA_SCHEDULE_NAME) ?: "Stock Market"
            
            notificationManager.showNotification(scheduleId, scheduleName)
        }
    }
}
