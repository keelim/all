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
package com.keelim.nandadiagnosis.data.db

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {
  private lateinit var dao: DataDao
  private lateinit var db: AppDatabase

  @Before
  fun createDb() {
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    // Using an in-memory database because the information stored here disappears when the
    // process is killed.
    db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
      // Allowing main thread queries, just for testing.
      .allowMainThreadQueries()
      .build()
    dao = db.dataDao()
  }

  @After
  @Throws(IOException::class)
  fun closeDb() {
    db.close()
  }
}
