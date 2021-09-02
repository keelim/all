package com.keelim.nandadiagnosis.initialize

import android.content.Context
import androidx.startup.Initializer
import com.mocklets.pluto.Pluto

class PlutoInitializer:Initializer<Unit> {
    override fun create(context: Context) {
        Pluto.initialize(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}