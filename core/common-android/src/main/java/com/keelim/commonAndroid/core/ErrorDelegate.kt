package com.keelim.commonAndroid.core

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Interface for centralized error handling across the application
 * Follows Android architecture guidelines for error management
 */
interface ErrorDelegate {
    /**
     * SharedFlow for observing errors across the application
     */
    val error: SharedFlow<Throwable>
    
    /**
     * Emit an error for centralized handling
     * 
     * @param throwable The throwable to emit and handle
     */
    fun emitError(throwable: Throwable)
    
    /**
     * Emit an error with additional context
     * 
     * @param throwable The throwable to emit and handle
     * @param context Additional context information
     */
    fun emitError(throwable: Throwable, context: String)
}

/**
 * Implementation of ErrorDelegate that integrates with Firebase Crashlytics
 * and provides centralized error handling with proper logging
 */
@Singleton
class ErrorDelegateImpl @Inject constructor() : ErrorDelegate {
    
    private val _error = MutableSharedFlow<Throwable>(
        replay = 0,
        extraBufferCapacity = 1
    )
    
    override val error: SharedFlow<Throwable> = _error
    
    /**
     * Emit error with Firebase Crashlytics logging and local error flow
     */
    override fun emitError(throwable: Throwable) {
        try {
            // Log to Firebase Crashlytics for remote monitoring
            Firebase.crashlytics.recordException(throwable)
            
            // Log locally for debugging
            Timber.e(throwable, "Error emitted through ErrorDelegate")
            
            // Emit to local flow for UI handling
            _error.tryEmit(throwable)
            
        } catch (e: Exception) {
            // Fallback logging if Firebase fails
            Timber.e(e, "Failed to emit error through ErrorDelegate")
            Timber.e(throwable, "Original error that failed to emit")
        }
    }
    
    /**
     * Emit error with additional context information
     */
    override fun emitError(throwable: Throwable, context: String) {
        try {
            // Add context to Crashlytics
            Firebase.crashlytics.apply {
                setCustomKey("error_context", context)
                recordException(throwable)
            }
            
            // Log with context
            Timber.e(throwable, "Error in context: %s", context)
            
            // Emit to flow
            _error.tryEmit(throwable)
            
        } catch (e: Exception) {
            // Fallback logging
            Timber.e(e, "Failed to emit error with context: %s", context)
            Timber.e(throwable, "Original error that failed to emit")
        }
    }
}
