package com.keelim.setting.di

import android.content.Context
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

data class DeviceInfo(
    val deviceName: String,
    val deviceBrand: String,
    val deviceModel: String,
    val versionName: String?,
)

interface DeviceInfoSource {
    fun getDeviceInfo(): Flow<DeviceInfo?>
}

class DeviceInfoSourceImpl @Inject constructor(
    @ApplicationContext val context: Context,
) : DeviceInfoSource {
    override fun getDeviceInfo(): Flow<DeviceInfo?> = flow {
        val deviceInfo = try {
            val deviceModel = Build.MODEL
            val deviceBrand = Build.MANUFACTURER
            val deviceName = Build.DEVICE
            val versionName = context.packageManager?.getPackageInfo(
                context.packageName,
                0,
            )?.versionName
            val info = DeviceInfo(
                deviceModel = deviceModel,
                deviceBrand = deviceBrand,
                deviceName = deviceName,
                versionName = versionName,
            )
            info
        } catch (e: Throwable) {
            Timber.e(e.message)
            null
        }
        emit(deviceInfo)
    }
}
