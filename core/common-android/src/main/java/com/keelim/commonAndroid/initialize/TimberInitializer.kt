package com.keelim.commonAndroid.initialize

import android.content.Context
import androidx.startup.Initializer
import com.keelim.commonAndroid.BuildConfig
import com.keelim.commonAndroid.util.CrashlyticsTree
import timber.log.Timber

class TimberInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        Timber.plant(
            if (BuildConfig.DEBUG) Timber.DebugTree() else CrashlyticsTree(),
        )
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
