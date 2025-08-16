package com.keelim.commonAndroid.ui.crash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel for crash reporting functionality
 * Follows MVVM pattern with proper state management using StateFlow
 */
@HiltViewModel
class CrashViewModel @Inject constructor() : ViewModel() {
    
    private val _isLogging = MutableStateFlow(false)
    val isLogging: StateFlow<Boolean> = _isLogging.asStateFlow()
    
    private val _crashData = MutableStateFlow<CrashData?>(null)
    val crashData: StateFlow<CrashData?> = _crashData.asStateFlow()
    
    /**
     * Log crash information for debugging purposes
     * 
     * @param errorMessage The error message to log
     * @param deviceInfo Additional device information
     */
    fun logCrash(errorMessage: String, deviceInfo: String) {
        viewModelScope.launch {
            try {
                _isLogging.value = true
                
                val crashData = CrashData(
                    errorMessage = errorMessage,
                    deviceInfo = deviceInfo,
                    timestamp = System.currentTimeMillis()
                )
                
                _crashData.value = crashData
                
                // Log to Timber for debugging
                Timber.e("Crash logged: %s", crashData.toString())
                
            } catch (e: Exception) {
                Timber.e(e, "Error logging crash data")
            } finally {
                _isLogging.value = false
            }
        }
    }
    
    /**
     * Clear crash data
     */
    fun clearCrashData() {
        _crashData.value = null
    }
}

/**
 * Data class representing crash information
 * Follows Android naming conventions for data classes
 */
data class CrashData(
    val errorMessage: String,
    val deviceInfo: String,
    val timestamp: Long
)
