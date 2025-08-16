package com.keelim.commonAndroid.ui.crash

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.content.getSystemService
import com.keelim.composeutil.ui.theme.KeelimTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlin.system.exitProcess

/**
 * Activity for displaying crash reports and handling app restart functionality
 * Follows Android architecture guidelines and proper error handling
 */
@AndroidEntryPoint
class CrashReportActivity : ComponentActivity() {
    
    companion object {
        const val EXTRA_ERROR_MESSAGE = "error"
        private const val RESTART_DELAY_MS = 1000L
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val errorMessage: String = intent.getStringExtra(EXTRA_ERROR_MESSAGE) 
            ?: getString(com.keelim.commonAndroid.R.string.crash_report_message)
            
        // Log crash for debugging
        Timber.e("Crash report displayed with error: %s", errorMessage)
        
        enableEdgeToEdge()
        setContent {
            KeelimTheme {
                CrashRoute(
                    errorMessage = errorMessage,
                    onAppRefresh = { restartApp(this) },
                )
            }
        }
    }

    /**
     * Restart the application after a crash
     * Uses AlarmManager for proper app restart with delay
     * 
     * @param context Application context for restart
     */
    private fun restartApp(context: Context) {
        try {
            val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
            
            if (intent == null) {
                Timber.e("Could not get launch intent for package: %s", context.packageName)
                return
            }
            
            val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_CANCEL_CURRENT
            }
            
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                pendingIntentFlags,
            )

            val alarmManager = context.getSystemService<AlarmManager>()
            
            if (alarmManager == null) {
                Timber.e("AlarmManager not available")
                return
            }
            
            alarmManager.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + RESTART_DELAY_MS,
                pendingIntent,
            )
            
            Timber.i("App restart scheduled")
            
            // Exit the app
            exitProcess(0)
            
        } catch (e: Exception) {
            Timber.e(e, "Error restarting app")
            // Fallback: just exit the app
            exitProcess(0)
        }
    }
}
