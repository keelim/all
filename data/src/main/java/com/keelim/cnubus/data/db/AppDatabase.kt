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
package com.keelim.cnubus.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.keelim.cnubus.data.db.dao.CommentDao
import com.keelim.cnubus.data.db.dao.StationDao
import com.keelim.cnubus.data.db.entity.Comment
import com.keelim.cnubus.data.db.entity.StationEntity
import com.keelim.cnubus.data.db.entity.StationSubwayCrossRefEntity
import com.keelim.cnubus.data.db.entity.SubwayEntity

@Database(
    entities = [
        StationEntity::class,
        SubwayEntity::class,
        StationSubwayCrossRefEntity::class,
        Comment::class
    ],
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ],
    version = 2,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun daoStation(): StationDao
    abstract fun daoComment(): CommentDao
}
