package com.keelim.commonAndroid

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.keelim.commonAndroid.util.ComponentLogger
import com.keelim.commonAndroid.util.CrashHandler
import com.keelim.common.maintenance.MaintenanceChecker
import jakarta.inject.Inject

open class BaseApplication : Application(), ImageLoaderFactory {
    @Inject
    internal lateinit var imageLoader: dagger.Lazy<ImageLoader>

    @Inject
    internal lateinit var componentLogger: dagger.Lazy<ComponentLogger>

    @Inject
    internal lateinit var crashHandler: dagger.Lazy<CrashHandler>

    @Inject
    internal lateinit var maintenanceChecker: dagger.Lazy<MaintenanceChecker>

    override fun onCreate() {
        super.onCreate()
        componentLogger.get().initialize()
        maintenanceChecker.get().initialize()
        Thread.setDefaultUncaughtExceptionHandler(crashHandler.get())
    }

    override fun newImageLoader(): ImageLoader = imageLoader.get()
}
