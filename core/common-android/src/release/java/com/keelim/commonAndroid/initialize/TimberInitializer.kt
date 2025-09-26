package com.keelim.commonAndroid.initialize

import android.content.Context
import androidx.startup.Initializer
import com.keelim.commonAndroid.util.CrashlyticsTree
import com.keelim.core.data.initialize.FirebaseInitializer
import timber.log.Timber

class TimberInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        Timber.plant(CrashlyticsTree())
    }

    override fun dependencies() = listOf(
        FirebaseInitializer::class.java,
    )
}
