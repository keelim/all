/*
 * Designed and developed by 2021 keelim (Jaehyun Kim)
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
package com.keelim.comssa.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.keelim.comssa.data.db.dao.SearchDao
import com.keelim.comssa.data.db.entity.Search
import java.io.File

@Database(entities = [Search::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
  abstract val searchDao: SearchDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
      if (INSTANCE == null) {
        synchronized(AppDatabase::class) {
          INSTANCE = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "Search"
          )
            .createFromFile(File(context.getExternalFilesDir(null), "comssa.db"))
            .allowMainThreadQueries()
            .build()
        }
      }
      return INSTANCE!!
    }
  }
}
