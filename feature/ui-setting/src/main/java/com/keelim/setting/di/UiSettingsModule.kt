package com.keelim.setting.di

import com.keelim.common.maintenance.MaintenanceChecker
import com.keelim.setting.worker.MaintenanceCheckerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface UiSettingsModule {
    @Binds
    @Singleton
    fun bindsDeviceInfoModule(
        deviceInfo: DeviceInfoSourceImpl,
    ): DeviceInfoSource

    @Binds
    @Singleton
    fun bindsMaintenanceChecker(
        checker: MaintenanceCheckerImpl,
    ): MaintenanceChecker
}
