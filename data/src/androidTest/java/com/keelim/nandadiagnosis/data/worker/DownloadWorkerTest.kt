package com.keelim.nandadiagnosis.data.worker

import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.work.testing.TestListenableWorkerBuilder
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Test

class DownloadWorkerTest{

    @Test
    fun testDownloadWork() {
        // Create Work Request
        val context = getTargetContext()
        val work = TestListenableWorkerBuilder<DownloadWorker>(context).build()
        runBlocking {
            val result = work.doWork()
            // Assert
            assertNotNull(result)
        }
    }
}