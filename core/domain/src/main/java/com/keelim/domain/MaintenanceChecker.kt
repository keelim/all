package com.keelim.domain

import kotlinx.coroutines.flow.StateFlow

interface MaintenanceChecker {
    fun initialize()
    val isUnderMaintenance: StateFlow<Boolean>
}
