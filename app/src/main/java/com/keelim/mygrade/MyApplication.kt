package com.keelim.mygrade

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.google.android.material.color.DynamicColors
import com.keelim.mygrade.notification.NotificationChannels
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
        NotificationChannels.initialize(this)
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}