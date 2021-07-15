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
package com.keelim.nandadiagnosis.data.repository

import com.keelim.nandadiagnosis.data.db.entity.History
import com.keelim.nandadiagnosis.data.db.entity.NandaEntity
import com.keelim.nandadiagnosis.data.db.entity.NandaEntity2

interface IORepository {

  suspend fun getNandaList(): List<NandaEntity2>

  suspend fun getLocalNandaList(): List<NandaEntity2>

  suspend fun insertNandaItem(nanda: NandaEntity2): Long

  suspend fun insertNandaList(nanda: List<NandaEntity2>)

  suspend fun updateNanda(nanda: NandaEntity2)

  suspend fun getNandaItem(uid: Long): NandaEntity2?

  suspend fun deleteAll()

  suspend fun deleteNandaItem(uid: Long)

  // production =>
  suspend fun getFavoriteList(): List<NandaEntity>

  suspend fun getSearchList(keyword: String?): List<NandaEntity>

  suspend fun getHistories(): List<History>

  suspend fun saveHistory(keyword: String)

  suspend fun deleteHistory(keyword: String)

  suspend fun updateFavorite(favorite:Int, id:Int)
}
