package com.keelim.nandadiagnosis.initialize

import android.content.Context
import androidx.startup.Initializer
import com.keelim.nandadiagnosis.BuildConfig
import com.keelim.nandadiagnosis.utils.CrashlyticsTree
import timber.log.Timber

class TimberInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashlyticsTree())
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}