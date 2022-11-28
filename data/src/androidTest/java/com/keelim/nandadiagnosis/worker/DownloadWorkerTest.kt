/*
 * Designed and developed by 2020 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keelim.nandadiagnosis.worker

import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.testing.TestListenableWorkerBuilder
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Test

class DownloadWorkerTest {

    @Test
    fun testDownloadWork() {
        // Create Work Request
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val work = TestListenableWorkerBuilder<com.keelim.nandadiagnosis.worker.DownloadWorker>(context).build()
        runBlocking {
            val result = work.doWork()
            // Assert
            assertNotNull(result)
        }
    }
}
