package com.keelim.setting.screen.maintenance

import androidx.lifecycle.ViewModel
import com.keelim.domain.MaintenanceChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class MaintenanceViewModel @Inject constructor(
    maintenanceChecker: MaintenanceChecker,
) : ViewModel() {
    val isUnderMaintenance = maintenanceChecker.isUnderMaintenance
}
