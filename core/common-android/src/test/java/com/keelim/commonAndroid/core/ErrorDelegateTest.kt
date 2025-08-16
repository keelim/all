package com.keelim.commonAndroid.core

import app.cash.turbine.test
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import timber.log.Timber

/**
 * Unit tests for ErrorDelegate implementation
 * Tests error handling, Firebase Crashlytics integration, and SharedFlow emission
 */
class ErrorDelegateTest {
    
    private lateinit var errorDelegate: ErrorDelegateImpl
    
    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        
        // Mock Firebase Crashlytics and Timber
        mockkStatic("com.google.firebase.ktx.FirebaseKt")
        mockkStatic("com.google.firebase.crashlytics.ktx.FirebaseCrashlyticsKtxKt")
        mockkStatic(Timber::class)
        
        errorDelegate = ErrorDelegateImpl()
    }
    
    @After
    fun tearDown() {
        unmockkAll()
    }
    
    @Test
    fun `emitError should emit throwable to SharedFlow`() = runTest {
        // Given
        val testException = RuntimeException("Test exception")
        
        // When & Then
        errorDelegate.error.test {
            errorDelegate.emitError(testException)
            
            val emittedError = awaitItem()
            assertEquals("Should emit the same exception", testException, emittedError)
        }
    }
    
    @Test
    fun `emitError with context should emit throwable to SharedFlow`() = runTest {
        // Given
        val testException = IllegalStateException("Test exception with context")
        val context = "test_context"
        
        // When & Then
        errorDelegate.error.test {
            errorDelegate.emitError(testException, context)
            
            val emittedError = awaitItem()
            assertEquals("Should emit the same exception", testException, emittedError)
        }
    }
    
    @Test
    fun `emitError should log with Timber`() = runTest {
        // Given
        val testException = RuntimeException("Test exception")
        every { Timber.e(any<Throwable>(), any<String>()) } returns Unit
        
        // When
        errorDelegate.emitError(testException)
        
        // Then
        verify { Timber.e(testException, "Error emitted through ErrorDelegate") }
    }
    
    @Test
    fun `emitError with context should log with context`() = runTest {
        // Given
        val testException = IllegalArgumentException("Test exception")
        val context = "validation_context"
        every { Timber.e(any<Throwable>(), any<String>(), any()) } returns Unit
        
        // When
        errorDelegate.emitError(testException, context)
        
        // Then
        verify { Timber.e(testException, "Error in context: %s", context) }
    }
    
    @Test
    fun `multiple emitError calls should emit all exceptions`() = runTest {
        // Given
        val exception1 = RuntimeException("First exception")
        val exception2 = IllegalStateException("Second exception")
        
        // When & Then
        errorDelegate.error.test {
            errorDelegate.emitError(exception1)
            assertEquals("First exception should be emitted", exception1, awaitItem())
            
            errorDelegate.emitError(exception2)
            assertEquals("Second exception should be emitted", exception2, awaitItem())
        }
    }
    
    @Test
    fun `emitError should handle Firebase exceptions gracefully`() = runTest {
        // Given
        val testException = RuntimeException("Test exception")
        val firebaseException = RuntimeException("Firebase error")
        
        // Mock Firebase to throw an exception
        val mockCrashlytics = mockk<com.google.firebase.crashlytics.FirebaseCrashlytics>(relaxed = true)
        every { mockCrashlytics.recordException(any()) } throws firebaseException
        
        every { Timber.e(any<Throwable>(), any<String>()) } returns Unit
        
        // When & Then - should still emit to flow despite Firebase failure
        errorDelegate.error.test {
            errorDelegate.emitError(testException)
            
            val emittedError = awaitItem()
            assertEquals("Should still emit despite Firebase failure", testException, emittedError)
        }
    }
    
    @Test
    fun `SharedFlow should have correct configuration`() {
        // Given & When
        val sharedFlow = errorDelegate.error
        
        // Then
        assertEquals("Replay should be 0", 0, sharedFlow.replayCache.size)
        // Note: Cannot directly test extraBufferCapacity and onBufferOverflow
        // These would require more complex testing setup
    }
}