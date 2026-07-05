package com.keelim.arducon.ui.screen.device

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.location.LocationManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.ContextCompat
import kotlin.math.abs

internal object DeviceHardwareTests {
    fun checkCamera(context: Context): DeviceTestOutcome {
        return if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            DeviceTestOutcome.pass(DeviceTestMessage.CapabilityAvailable)
        } else {
            DeviceTestOutcome.fail(DeviceTestMessage.MissingFeature)
        }
    }

    fun probeMicrophone(context: Context): DeviceTestOutcome {
        if (!context.hasPermission(Manifest.permission.RECORD_AUDIO)) {
            return DeviceTestOutcome.fail(DeviceTestMessage.PermissionDenied)
        }
        if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
            return DeviceTestOutcome.fail(DeviceTestMessage.MissingFeature)
        }

        val bufferSize = AudioRecord.getMinBufferSize(
            MICROPHONE_SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
        )
        if (bufferSize <= 0) {
            return DeviceTestOutcome.fail(DeviceTestMessage.MissingFeature)
        }

        val recorder = try {
            AudioRecord(
                MediaRecorder.AudioSource.MIC,
                MICROPHONE_SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize,
            )
        } catch (throwable: Throwable) {
            return DeviceTestOutcome.fail(DeviceTestMessage.Failed, throwable.safeName())
        }

        return try {
            if (recorder.state != AudioRecord.STATE_INITIALIZED) {
                DeviceTestOutcome.fail(DeviceTestMessage.MissingFeature)
            } else {
                val sample = ShortArray(bufferSize.coerceAtMost(MAX_MICROPHONE_SAMPLE))
                recorder.startRecording()
                val readCount = recorder.read(sample, 0, sample.size)
                if (readCount > 0) {
                    val peak = sample.take(readCount).maxOf { abs(it.toInt()) }
                    DeviceTestOutcome.pass(DeviceTestMessage.MicrophoneCaptured, peak.toString())
                } else {
                    DeviceTestOutcome.fail(DeviceTestMessage.Failed, readCount.toString())
                }
            }
        } catch (throwable: Throwable) {
            DeviceTestOutcome.fail(DeviceTestMessage.Failed, throwable.safeName())
        } finally {
            runCatching {
                if (recorder.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                    recorder.stop()
                }
            }
            recorder.release()
        }
    }

    fun vibrate(context: Context): DeviceTestOutcome {
        val vibrator = ContextCompat.getSystemService(context, Vibrator::class.java)
            ?: return DeviceTestOutcome.fail(DeviceTestMessage.MissingFeature)
        if (!vibrator.hasVibrator()) {
            return DeviceTestOutcome.fail(DeviceTestMessage.MissingFeature)
        }

        return try {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    VIBRATION_DURATION_MILLIS,
                    VibrationEffect.DEFAULT_AMPLITUDE,
                ),
            )
            DeviceTestOutcome.pass(DeviceTestMessage.VibrationSent)
        } catch (throwable: Throwable) {
            DeviceTestOutcome.fail(DeviceTestMessage.Failed, throwable.safeName())
        }
    }

    fun summarizeSensors(context: Context): DeviceTestOutcome {
        val sensorManager = context.getSystemService(SensorManager::class.java)
            ?: return DeviceTestOutcome.fail(DeviceTestMessage.MissingFeature)
        val sensorCount = sensorManager.getSensorList(Sensor.TYPE_ALL).size
        return if (sensorCount > 0) {
            DeviceTestOutcome.pass(DeviceTestMessage.SensorsFound, sensorCount.toString())
        } else {
            DeviceTestOutcome.fail(DeviceTestMessage.MissingFeature)
        }
    }

    fun checkNetwork(context: Context): DeviceTestOutcome {
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
            ?: return DeviceTestOutcome.fail(DeviceTestMessage.MissingFeature)
        val activeNetwork = connectivityManager.activeNetwork
            ?: return DeviceTestOutcome.fail(DeviceTestMessage.NetworkUnavailable)
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
            ?: return DeviceTestOutcome.fail(DeviceTestMessage.NetworkUnavailable)
        if (!capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
            return DeviceTestOutcome.fail(DeviceTestMessage.NetworkUnavailable)
        }

        return DeviceTestOutcome.pass(
            message = DeviceTestMessage.NetworkAvailable,
            detail = capabilities.transportName(),
        )
    }

    fun checkLocationReadiness(context: Context): DeviceTestOutcome {
        val hasLocationPermission = context.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) ||
            context.hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        if (!hasLocationPermission) {
            return DeviceTestOutcome.fail(DeviceTestMessage.PermissionDenied)
        }

        val locationManager = context.getSystemService(LocationManager::class.java)
            ?: return DeviceTestOutcome.fail(DeviceTestMessage.MissingFeature)
        val providers = locationManager.getProviders(true)
        return if (providers.isEmpty()) {
            DeviceTestOutcome.skipped(DeviceTestMessage.NoLocationProvider)
        } else {
            DeviceTestOutcome.pass(
                message = DeviceTestMessage.LocationReady,
                detail = providers.joinToString(),
            )
        }
    }
}

internal fun Context.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

private fun NetworkCapabilities.transportName(): String {
    return when {
        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "Wi-Fi"
        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Cellular"
        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Ethernet"
        hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> "Bluetooth"
        hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> "VPN"
        else -> "Other"
    }
}

private fun Throwable.safeName(): String = javaClass.simpleName.ifBlank { "Unknown" }

private const val MICROPHONE_SAMPLE_RATE = 8_000
private const val MAX_MICROPHONE_SAMPLE = 2_048
private const val VIBRATION_DURATION_MILLIS = 120L
