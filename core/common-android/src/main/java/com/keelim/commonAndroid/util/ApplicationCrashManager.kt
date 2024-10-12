package com.keelim.commonAndroid.util


import android.content.Context
import android.content.Intent
import com.keelim.commonAndroid.ui.crash.CrashReportActivity
import java.lang.Thread.UncaughtExceptionHandler

/**
 * The uncaught exception handler for the application.
 */
class CrashHandler(private val context: Context) : UncaughtExceptionHandler {

    fun init() {
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    /**
     * Catch all uncaught exception and log it.
     */
    override fun uncaughtException(p0: Thread, p1: Throwable) {
        context.startActivity(Intent(context, CrashReportActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra("error", p1.stackTraceToString())
        })
    }
}
