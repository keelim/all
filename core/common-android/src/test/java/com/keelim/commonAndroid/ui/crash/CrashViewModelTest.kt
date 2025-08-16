package com.keelim.commonAndroid.ui.crash

import app.cash.turbine.test
import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for CrashViewModel following JUnit best practices
 * Tests MVVM pattern implementation and StateFlow behavior
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CrashViewModelTest {
    
    private val testDispatcher = StandardTestDispatcher()
    
    private lateinit var viewModel: CrashViewModel
    
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = CrashViewModel()
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }
    
    @Test
    fun `initial state should be correct`() = runTest {
        // Given - fresh ViewModel
        
        // When - observing initial state
        viewModel.isLogging.test {
            val isLogging = awaitItem()
            
            // Then - should not be logging initially
            assertFalse("Should not be logging initially", isLogging)
        }
        
        viewModel.crashData.test {
            val crashData = awaitItem()
            
            // Then - should have no crash data initially
            assertNull("Should have no crash data initially", crashData)
        }
    }
    
    @Test
    fun `logCrash should update state correctly`() = runTest {
        // Given
        val errorMessage = "Test error message"
        val deviceInfo = "Test device info"
        
        // When
        viewModel.logCrash(errorMessage, deviceInfo)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        viewModel.crashData.test {
            val crashData = awaitItem()
            
            assertNotNull("Crash data should not be null", crashData)
            assertEquals("Error message should match", errorMessage, crashData!!.errorMessage)
            assertEquals("Device info should match", deviceInfo, crashData.deviceInfo)
            assertTrue("Timestamp should be positive", crashData.timestamp > 0)
        }
        
        viewModel.isLogging.test {
            val isLogging = awaitItem()
            
            assertFalse("Should not be logging after completion", isLogging)
        }
    }
    
    @Test
    fun `logCrash should set logging state during operation`() = runTest {
        // Given
        val errorMessage = "Test error"
        val deviceInfo = "Test device"
        
        // When & Then
        viewModel.isLogging.test {
            // Initial state
            assertEquals(false, awaitItem())
            
            // Start logging
            viewModel.logCrash(errorMessage, deviceInfo)
            
            // Should be logging during operation
            assertEquals(true, awaitItem())
            
            // Complete the operation
            testDispatcher.scheduler.advanceUntilIdle()
            
            // Should stop logging after completion
            assertEquals(false, awaitItem())
        }
    }
    
    @Test
    fun `clearCrashData should clear crash data`() = runTest {
        // Given - crash data exists
        val errorMessage = "Test error message"
        val deviceInfo = "Test device info"
        
        viewModel.logCrash(errorMessage, deviceInfo)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // When - clearing crash data
        viewModel.clearCrashData()
        
        // Then - crash data should be null
        viewModel.crashData.test {
            val crashData = awaitItem()
            
            assertNull("Crash data should be cleared", crashData)
        }
    }
    
    @Test
    fun `multiple logCrash calls should update with latest data`() = runTest {
        // Given
        val firstError = "First error"
        val firstDevice = "First device"
        val secondError = "Second error"
        val secondDevice = "Second device"
        
        // When
        viewModel.logCrash(firstError, firstDevice)
        testDispatcher.scheduler.advanceUntilIdle()
        
        viewModel.logCrash(secondError, secondDevice)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then - should have latest data
        viewModel.crashData.test {
            val crashData = awaitItem()
            
            assertNotNull("Crash data should exist", crashData)
            assertEquals("Should have latest error message", secondError, crashData!!.errorMessage)
            assertEquals("Should have latest device info", secondDevice, crashData.deviceInfo)
        }
    }
}

/**
 * Unit tests for CrashData data class
 */
class CrashDataTest {
    
    @Test
    fun `CrashData should be created with correct properties`() {
        // Given
        val errorMessage = "Test error"
        val deviceInfo = "Test device"
        val timestamp = System.currentTimeMillis()
        
        // When
        val crashData = CrashData(errorMessage, deviceInfo, timestamp)
        
        // Then
        assertEquals("Error message should match", errorMessage, crashData.errorMessage)
        assertEquals("Device info should match", deviceInfo, crashData.deviceInfo)
        assertEquals("Timestamp should match", timestamp, crashData.timestamp)
    }
    
    @Test
    fun `CrashData toString should contain all properties`() {
        // Given
        val crashData = CrashData(
            errorMessage = "Test error",
            deviceInfo = "Test device",
            timestamp = 1234567890L
        )
        
        // When
        val stringRepresentation = crashData.toString()
        
        // Then
        assertTrue("Should contain error message", stringRepresentation.contains("Test error"))
        assertTrue("Should contain device info", stringRepresentation.contains("Test device"))
        assertTrue("Should contain timestamp", stringRepresentation.contains("1234567890"))
    }
    
    @Test
    fun `CrashData equals should work correctly`() {
        // Given
        val crashData1 = CrashData("error", "device", 123L)
        val crashData2 = CrashData("error", "device", 123L)
        val crashData3 = CrashData("different", "device", 123L)
        
        // Then
        assertEquals("Same data should be equal", crashData1, crashData2)
        assertTrue("Same data should have same hashCode", crashData1.hashCode() == crashData2.hashCode())
        assertFalse("Different data should not be equal", crashData1 == crashData3)
    }
}