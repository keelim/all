package com.keelim.mygrade

import android.app.Application
import com.google.android.material.color.DynamicColors
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.keelim.commonAndroid.util.ComponentLogger
import com.keelim.mygrade.utils.AppOpenManager
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
        componentLogger.initialize(this)
        Firebase.initialize(this)
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}
