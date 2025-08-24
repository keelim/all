package com.keelim.commonAndroid

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.keelim.commonAndroid.util.ComponentLogger
import com.keelim.commonAndroid.util.CrashHandler
import javax.inject.Inject

open class BaseApplication : Application(), ImageLoaderFactory {
    @Inject
    lateinit var imageLoader: dagger.Lazy<ImageLoader>

    @Inject
    lateinit var componentLogger: dagger.Lazy<ComponentLogger>
    @Inject
    lateinit var crashHandler: dagger.Lazy<CrashHandler>

    override fun onCreate() {
        super.onCreate()
        componentLogger.get().initialize()
        Thread.setDefaultUncaughtExceptionHandler(crashHandler.get())
    }

    override fun newImageLoader(): ImageLoader = imageLoader.get()
}
