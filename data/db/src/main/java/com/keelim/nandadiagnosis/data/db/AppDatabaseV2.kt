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

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

@Database(entities = [NandaEntity::class], version = 2, exportSchema = false)
abstract class AppDatabaseV2 : RoomDatabase() {
  abstract val dataDao: DataDaoV2

  companion object {
    @Volatile
    private var INSTANCE: AppDatabaseV2? = null

    fun getInstance(context: Context): AppDatabaseV2? {
      if (INSTANCE == null) {
        synchronized(AppDatabaseV2::class) {
          INSTANCE = Room.databaseBuilder(
            context.applicationContext,
            AppDatabaseV2::class.java,
            "nanda"
          )
            .createFromFile(File(context.getExternalFilesDir(null), "nanda.db"))
//                            .createFromFile(context.getDatabasePath("nanda.db"))
            .allowMainThreadQueries()
            .build()
        }
      }
      return INSTANCE
    }
  }
}
