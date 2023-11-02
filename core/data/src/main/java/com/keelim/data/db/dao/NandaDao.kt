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
package com.keelim.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.keelim.data.model.entity.NandaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NandaDao {
    @Query("SELECT * FROM nanda WHERE reason LIKE  '%' || :keyword || '%'")
    suspend fun search(keyword: String?): List<NandaEntity>

    @Query("SELECT * FROM nanda WHERE favorite = 1")
    suspend fun favorites(): List<NandaEntity>

    @Query("SELECT * FROM nanda WHERE category = :number")
    suspend fun get(number: Int?): List<NandaEntity>

    @Query("SELECT * FROM nanda ORDER BY nanda_id DESC")
    suspend fun getNanda(): NandaEntity?

    @Query("SELECT * FROM nanda")
    suspend fun getAll(): List<NandaEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNanda(nanda: NandaEntity)

    @Delete
    suspend fun delete(nanda: NandaEntity)

    @Query("UPDATE nanda SET favorite=:favorite WHERE nanda_id = :id")
    suspend fun favoriteUpdate(favorite: Int, id: Int)

    @Query("SELECT * FROM nanda WHERE reason LIKE  '%' || :query || '%'")
    fun getSearchFlow(query: String): Flow<List<NandaEntity>>

    @Transaction
    @Query("SELECT * FROM nanda")
    fun getSearchData(): Flow<List<NandaEntity>>
}
