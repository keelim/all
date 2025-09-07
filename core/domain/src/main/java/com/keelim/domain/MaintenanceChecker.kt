package com.keelim.domain

import androidx.lifecycle.DefaultLifecycleObserver
import kotlinx.coroutines.flow.StateFlow

interface MaintenanceChecker : DefaultLifecycleObserver {
    val isUnderMaintenance: StateFlow<Boolean>
}
