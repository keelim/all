package com.keelim.testing.util

import io.kotest.core.listeners.AfterEachListener
import io.kotest.core.listeners.BeforeEachListener
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

/**
 * A Kotest extension that sets the Main dispatcher to [testDispatcher]
 * for the duration of the test.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : BeforeEachListener, AfterEachListener {
    override suspend fun beforeEach(testCase: TestCase) {
        Dispatchers.setMain(testDispatcher)
    }

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        Dispatchers.resetMain()
    }
}
