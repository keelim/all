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
package com.keelim.comssa.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.keelim.comssa.data.db.entity.Search
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface SearchDao {

  @Query("SELECT * FROM Search WHERE title like '%'|| :keyword || '%'")
  fun getSearch(keyword: String): List<Search>

  @Query("UPDATE Search SET favorite=:favorite WHERE id = :id")
  fun favoriteUpdate(favorite: Int, id: Int)

  @Query("SELECT * FROM Search WHERE favorite == 1")
  fun getFavorite(): List<Search>

  @Query("SELECT * FROM Search WHERE title like '%'|| :keyword || '%'")
  fun getSearch2(keyword: String): Flow<List<Search>>

  @Query("SELECT * FROM Search WHERE favorite == 1")
  fun getFavorite2(): Flow<List<Search>>

  fun getSearchDistinctUntilChanged(keyword: String) = getSearch2(keyword).distinctUntilChanged()
}
