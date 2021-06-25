package com.keelim.comssa

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.google.android.play.core.internal.ad
import com.keelim.comssa.utils.AppOpenManager
import com.keelim.comssa.utils.ComponentLogger
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import timber.log.Timber

@HiltAndroidApp
class MyApplication: Application() {
    @Inject
    lateinit var componentLogger:ComponentLogger

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        componentLogger.initialize(this)
        MobileAds.initialize(this) {}
        val appOpenManager = AppOpenManager(this)
    }


}