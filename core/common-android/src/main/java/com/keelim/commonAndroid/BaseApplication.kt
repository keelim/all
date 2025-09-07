package com.keelim.commonAndroid

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.keelim.commonAndroid.util.ComponentLogger
import com.keelim.commonAndroid.util.CrashHandler
import com.keelim.domain.MaintenanceChecker
import javax.inject.Inject

open class BaseApplication : Application(), ImageLoaderFactory {
    @Inject
    lateinit var imageLoader: dagger.Lazy<ImageLoader>

    @Inject
    lateinit var componentLogger: dagger.Lazy<ComponentLogger>

    @Inject
    lateinit var crashHandler: dagger.Lazy<CrashHandler>

    @Inject
    lateinit var maintenanceChecker: dagger.Lazy<MaintenanceChecker>

    override fun onCreate() {
        super.onCreate()
        componentLogger.get().initialize()
        ProcessLifecycleOwner.get().lifecycle.addObserver(maintenanceChecker.get())
        Thread.setDefaultUncaughtExceptionHandler(crashHandler.get())
    }

    override fun newImageLoader(): ImageLoader = imageLoader.get()
}
