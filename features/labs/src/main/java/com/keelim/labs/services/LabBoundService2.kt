package com.keelim.labs.services

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import com.keelim.common.extensions.toast
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class LabBoundService2 : Service() {
    private val messenger by lazy { Messenger(IncomingHandler()) }
    override fun onBind(p0: Intent?): IBinder? {
        return messenger.binder
    }

    fun getCurrentTime(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            current.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        } else {
            SimpleDateFormat("HH:mm:ss MM/dd/yyyy", Locale.KOREA).format(Date())
        }
    }

    inner class IncomingHandler : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val data = msg.data
            val dataString = data.getString("MyString") ?: ""
            applicationContext.toast(dataString)
        }
    }
}