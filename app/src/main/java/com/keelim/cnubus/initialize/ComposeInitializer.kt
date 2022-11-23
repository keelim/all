package com.keelim.cnubus.initialize

import android.content.Context
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ProcessLifecycleInitializer
import androidx.startup.Initializer

class ComposeInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        ComposeView(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(
            ProcessLifecycleInitializer::class.java,
            TimberInitializer::class.java,
        )
    }
}
