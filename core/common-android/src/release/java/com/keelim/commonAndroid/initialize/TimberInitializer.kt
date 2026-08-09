package com.keelim.commonAndroid.initialize

import android.content.Context
import androidx.startup.Initializer

class TimberInitializer : Initializer<Unit> {
    override fun create(context: Context) = Unit

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
