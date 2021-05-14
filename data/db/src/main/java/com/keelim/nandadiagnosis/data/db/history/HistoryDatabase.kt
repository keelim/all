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
package com.keelim.nandadiagnosis.data.db.history

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.keelim.nandadiagnosis.data.db.AppDatabaseV2

@Database(entities = [History::class], version = 1)
abstract class HistoryDatabase : RoomDatabase() {
  abstract fun historyDao(): HistoryDao

  companion object {
    @Volatile
    private var INSTANCE: HistoryDatabase? = null

    fun getInstance(context: Context): HistoryDatabase? {
      if (INSTANCE == null) {
        synchronized(AppDatabaseV2::class) {
          HistoryDatabase.INSTANCE = Room.databaseBuilder(
            context.applicationContext,
            HistoryDatabase::class.java,
            "BookSearchDB"
          )
            .build()
        }
      }
      return INSTANCE
    }
  }
}
