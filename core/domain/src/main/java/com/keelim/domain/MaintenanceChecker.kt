package com.keelim.domain

import androidx.lifecycle.DefaultLifecycleObserver
import kotlinx.coroutines.flow.StateFlow

interface MaintenanceChecker : DefaultLifecycleObserver {

    fun initialize()
    val isUnderMaintenance: StateFlow<Boolean>
}
