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
package com.keelim.nandadiagnosis.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.keelim.nandadiagnosis.data.db.AppDatabaseV2
import com.keelim.nandadiagnosis.data.db.entity.NandaEntity
import com.keelim.nandadiagnosis.data.db.entity.NandaEntity2
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @Inject
  lateinit var appDatabaseV2: AppDatabaseV2

  @Before
  fun init(){
    hiltRule.inject()
  }

  @Test
  fun roomTest() = runBlockingTest {
    val schema = NandaEntity(10001, "", "", "", "", 0, 0)
    appDatabaseV2.dataDao().insertNanda(schema)

    var resultSchema = appDatabaseV2.dataDao().getAll()[0]
    assertDbEquals(schema, resultSchema)
  }

  private fun assertDbEquals(expected: NandaEntity, actual: NandaEntity) {
    // id 는 자동생성되므로, 검증을 위해서 id의 동일성은 무시하자.
    assertEquals(expected.copy(nanda_id = 0), actual.copy(nanda_id = 0))
  }
}
