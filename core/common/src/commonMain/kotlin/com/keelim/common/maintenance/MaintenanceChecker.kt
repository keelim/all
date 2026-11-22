package com.keelim.common.maintenance

import kotlinx.coroutines.flow.StateFlow

interface MaintenanceChecker {
    fun initialize()
    val isUnderMaintenance: StateFlow<Boolean>
}
