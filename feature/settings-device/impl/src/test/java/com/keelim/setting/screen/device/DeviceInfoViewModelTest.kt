package com.keelim.setting.screen.device

import app.cash.turbine.test
import com.keelim.setting.di.DeviceInfo
import com.keelim.setting.di.DeviceInfoSource
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class DeviceInfoViewModelTest : FunSpec({
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)
    val sampleDeviceInfo = DeviceInfo(
        deviceName = "pixel",
        deviceBrand = "Google",
        deviceModel = "Pixel 9",
        versionName = "1.0.0",
        platform = "ANDROID",
        isSupported = true,
        board = "board",
        hardware = "hardware",
        product = "product",
        sdkLevel = 36,
        screenDensity = 480,
        screenWidthDp = 411,
        screenHeightDp = 891,
        supportedAbis = listOf("arm64-v8a"),
    )

    extension(mainDispatcherRule)

    fun createViewModel(flow: Flow<DeviceInfo?>): DeviceInfoViewModel {
        val deviceInfoSource = object : DeviceInfoSource {
            override fun getDeviceInfo(): Flow<DeviceInfo?> = flow
        }

        return DeviceInfoViewModel(deviceInfoSource)
    }

    test("uiState starts with empty device info") {
        val viewModel = createViewModel(emptyFlow())

        viewModel.uiState.value shouldBe DeviceInfo.empty()
    }

    test("null device info emissions map to empty device info") {
        val upstream = MutableStateFlow<DeviceInfo?>(sampleDeviceInfo)
        val viewModel = createViewModel(upstream)

        runTest(testDispatcher) {
            viewModel.uiState.test {
                awaitItem() shouldBe DeviceInfo.empty()

                advanceUntilIdle()
                awaitItem() shouldBe sampleDeviceInfo

                upstream.value = null
                advanceUntilIdle()

                awaitItem() shouldBe DeviceInfo.empty()
            }
        }
    }

    test("non-null device info is forwarded unchanged") {
        val upstream = MutableStateFlow<DeviceInfo?>(sampleDeviceInfo)
        val viewModel = createViewModel(upstream)

        runTest(testDispatcher) {
            viewModel.uiState.test {
                awaitItem() shouldBe DeviceInfo.empty()

                advanceUntilIdle()
                awaitItem() shouldBe sampleDeviceInfo
            }
        }
    }
})
