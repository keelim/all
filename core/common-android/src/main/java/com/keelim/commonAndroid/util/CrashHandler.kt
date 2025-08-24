package com.keelim.commonAndroid.util

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import androidx.core.graphics.createBitmap
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.lang.ref.WeakReference
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class CrashHandler @Inject constructor(
    private val application: Application,
) : Thread.UncaughtExceptionHandler {
    private val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
    private var currentActivity: WeakReference<Activity>? = null
    private val executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    init {
        application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                setCurrentActivity(activity)
            }

            override fun onActivityStarted(activity: Activity) {}

            override fun onActivityResumed(activity: Activity) {
                setCurrentActivity(activity)
            }

            override fun onActivityPaused(activity: Activity) {
                setCurrentActivity(null)
            }

            override fun onActivityStopped(activity: Activity) {}

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityDestroyed(activity: Activity) {}
        })
    }

    fun setCurrentActivity(activity: Activity?) {
        currentActivity = activity?.let { WeakReference(it) }
    }

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        Timber.e("Uncaught exception detected on thread: ${'$'}{thread.name}")
        try {
            runBlocking {
                withTimeout(SCREENSHOT_TIMEOUT_MS) {
                    captureScreenshot(throwable)
                }
            }
            defaultHandler?.uncaughtException(thread, throwable)
        } catch (e: Exception) {
            Timber.e(e, "Failed to handle crash with screenshot")
            // Call the original handler
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }

    private fun captureScreenshot(throwable: Throwable) {
        val activity = currentActivity?.get()
        if (activity == null) {
            Timber.d("No current activity, not capturing screenshot")
            return
        }

        val root = activity.window.decorView.rootView
        if (root == null || root.width <= 0 || root.height <= 0 || !root.isShown) {
            Timber.d("Root view is invalid, not capturing screenshot")
            return
        }

        val window =  activity.window
        if (window == null) {
            Timber.d("Phone window is null, not capturing screenshot")
            return
        }

        val screenshot = createBitmap(root.width, root.height)
        try {
            PixelCopy.request(
                window,
                screenshot,
                { copyResult: Int ->
                    if (copyResult == PixelCopy.SUCCESS) {
                        Timber.d("Screenshot captured successfully")
                    } else {
                        Timber.d("Failed to capture screenshot")
                    }
                },
                Handler(Looper.getMainLooper())
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Timber.e(e, "Failed to capture screenshot")
        } finally {
            saveScreenshotToFile(screenshot, throwable)
        }
    }

    private fun saveScreenshotToFile(bitmap: Bitmap, throwable: Throwable) {
        executor.execute {
            try {
                val screenshotsDir = File(application.filesDir, SCREENSHOTS_DIR).apply {
                    if (!exists()) mkdirs()
                }
                // use kotlin date time now
                val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                val timestamp = "${now.year}${now.month.number.toString().padStart(2, '0')}${now.day.toString().padStart(2, '0')}_${now.hour.toString().padStart(2, '0')}${now.minute.toString().padStart(2, '0')}${now.second.toString().padStart(2, '0')}"
                val exceptionName = throwable.javaClass.simpleName
                val filename = "${timestamp}_${exceptionName}.jpg"
                val screenshotFile = File(screenshotsDir, filename)
                FileOutputStream(screenshotFile).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
                }
                Timber.d("Crash screenshot saved: ${'$'}{screenshotFile.absolutePath}")
            } catch (e: Exception) {
                Timber.e(e, "Failed to save screenshot")
            }
        }
    }

    companion object {
        private const val SCREENSHOTS_DIR = "crash_all_screenshots"
        private const val SCREENSHOT_TIMEOUT_MS = 3000L
    }
}
