package com.keelim.setting.di

import android.content.Context
import android.os.Build
import com.keelim.shared.getAppSupported
import com.keelim.shared.getPlatform
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

data class DeviceInfo(
    val deviceName: String,
    val deviceBrand: String,
    val deviceModel: String,
    val versionName: String,
    val platform: String,
    val isSupported: Boolean,
    val board: String,
    val hardware: String,
    val product: String,
    val sdkLevel: Int,
    val screenDensity: Int,
    val screenWidthDp: Int,
    val screenHeightDp: Int,
    val supportedAbis: List<String>,
) {
    companion object {
        fun empty(): DeviceInfo = DeviceInfo(
            deviceName = "",
            deviceBrand = "",
            deviceModel = "",
            versionName = "",
            platform = "",
            isSupported = false,
            board = "",
            hardware = "",
            product = "",
            sdkLevel = 0,
            screenDensity = 0,
            screenWidthDp = 0,
            screenHeightDp = 0,
            supportedAbis = emptyList(),
        )
    }
}

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
            val displayMetrics = context.resources.displayMetrics
            val configuration = context.resources.configuration

            val info = DeviceInfo(
                deviceModel = deviceModel,
                deviceBrand = deviceBrand,
                deviceName = deviceName,
                versionName = versionName ?: "",
                platform = getPlatform().name,
                isSupported = getAppSupported().isSupported,
                board = Build.BOARD,
                hardware = Build.HARDWARE,
                product = Build.PRODUCT,
                sdkLevel = Build.VERSION.SDK_INT,
                screenDensity = displayMetrics.densityDpi,
                screenWidthDp = configuration.screenWidthDp,
                screenHeightDp = configuration.screenHeightDp,
                supportedAbis = Build.SUPPORTED_ABIS.toList(),
            )
            info
        } catch (e: Throwable) {
            Timber.e(e.message)
            null
        }
        emit(deviceInfo)
    }
}
