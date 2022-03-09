package com.keelim.labs.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class LabBoundService : Service() {
    private val binder = LocalBinder()
    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }

    fun getCurrentTime():String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            current.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        } else {
            SimpleDateFormat("HH:mm:ss MM/dd/yyyy", Locale.KOREA).format(Date())
        }
    }

    inner class LocalBinder() : Binder() {
        fun getService(): LabBoundService {
            return this@LabBoundService
        }
    }
}