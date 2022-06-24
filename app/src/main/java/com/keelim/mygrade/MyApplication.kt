package com.keelim.mygrade

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.google.android.material.color.DynamicColors
import com.keelim.mygrade.notification.NotificationChannels
import com.keelim.mygrade.utils.AppOpenManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApplication: Application() {
    @Inject
    lateinit var appOpenManager: AppOpenManager
    override fun onCreate() {
        super.onCreate()
//        MobileAds.initialize(this)
//        appOpenManager.initialize(this)
        NotificationChannels.initialize(this)
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}
