package com.keelim.nandadiagnosis.initialize

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.startup.Initializer
import androidx.work.Configuration
import androidx.work.WorkManager
import javax.inject.Inject

class WorkManager: Initializer<WorkManager> {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun create(context: Context): WorkManager {
        WorkManager.initialize(context, Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build())
        return WorkManager.getInstance(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        // No dependencies on other libraries.
        return emptyList()
    }
}