package com.keelim.setting.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface UiSettingsModule {
    @Binds
    @ViewModelScoped
    fun bindsDeviceInfoModule(
        deviceInfo: DeviceInfoSourceImpl
    ): DeviceInfoSource
}
