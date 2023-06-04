package com.keelim.mygrade

import android.app.Application
import com.google.android.material.color.DynamicColors
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.keelim.commonAndroid.util.ComponentLogger
import com.keelim.mygrade.notification.NotificationChannels
import com.keelim.mygrade.utils.AppOpenManager
import com.keelim.mygrade.utils.Keys
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application() {
    @Inject
    lateinit var componentLogger: ComponentLogger

    @Inject
    lateinit var appOpenManager: AppOpenManager
    override fun onCreate() {
        super.onCreate()
//        MobileAds.initialize(this)
//        appOpenManager.initialize(this)
        componentLogger.initialize(this)
        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = Keys.fetchTime
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        NotificationChannels.initialize(this)
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}
