package com.keelim.commonAndroid.initialize

import android.content.Context
import androidx.startup.Initializer
import com.google.firebase.Firebase
import com.google.firebase.initialize
import com.keelim.commonAndroid.BuildConfig
import com.keelim.commonAndroid.util.CrashlyticsTree
import timber.log.Timber

class TimberInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>>  = listOf()
}
