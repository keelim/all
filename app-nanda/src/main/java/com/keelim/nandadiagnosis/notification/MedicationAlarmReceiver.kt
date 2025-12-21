package com.keelim.nandadiagnosis.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MedicationAlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationManager: MedicationNotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == MedicationNotificationManager.ACTION_SHOW_NOTIFICATION) {
            val medicationId = intent.getStringExtra(MedicationNotificationManager.EXTRA_MEDICATION_ID) ?: return
            val medicationName = intent.getStringExtra(MedicationNotificationManager.EXTRA_MEDICATION_NAME) ?: "Medication"
            val medicationDosage = intent.getStringExtra(MedicationNotificationManager.EXTRA_MEDICATION_DOSAGE) ?: ""

            notificationManager.showNotification(medicationId, medicationName, medicationDosage)
        }
    }
}
