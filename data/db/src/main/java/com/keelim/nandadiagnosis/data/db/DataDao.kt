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

import androidx.room.Dao
import androidx.room.Query

@Dao
interface DataDao {
    @Query("select * from nanda where reason like  '%' || :keyword || '%'")
    fun search(keyword: String?): List<NandaEntity>

    @Query("select * from nanda where category = :number")
    fun get(number: Int?): List<NandaEntity>
}
