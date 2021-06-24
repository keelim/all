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

import com.keelim.nandadiagnosis.data.db.AppDatabaseV2
import com.keelim.nandadiagnosis.data.db.entity.History
import com.keelim.nandadiagnosis.data.db.entity.NandaEntity
import com.keelim.nandadiagnosis.data.db.entity.NandaEntity2
import com.keelim.nandadiagnosis.data.network.NandaService
import com.keelim.nandadiagnosis.di.DefaultDispatcher
import com.keelim.nandadiagnosis.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class IORepositoryImpl @Inject constructor(
  private val nandaService: NandaService,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
  @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
  private val db: AppDatabaseV2,
) : IORepository {

  override suspend fun getNandaList(): List<NandaEntity2> = withContext(ioDispatcher) {
    val response = nandaService.getNandas()
    return@withContext if (response.isSuccessful) {
      response.body()?.items?.map { it.toEntity() } ?: listOf()
    } else {
      listOf()
    }
  }

  override suspend fun getLocalNandaList(): List<NandaEntity2> = withContext(ioDispatcher) {
    TODO("Not yet implemented")
  }

  override suspend fun insertNandaItem(nanda: NandaEntity2): Long = withContext(ioDispatcher) {
    TODO("Not yet implemented")
  }

  override suspend fun insertNandaList(nanda: List<NandaEntity2>) = withContext(ioDispatcher) {
    TODO("Not yet implemented")
  }

  override suspend fun updateNanda(nanda: NandaEntity2) = withContext(ioDispatcher) {
    TODO("Not yet implemented")
  }

  override suspend fun getNandaItem(uid: Long): NandaEntity2? = withContext(ioDispatcher) {
    TODO("Not yet implemented")
  }

  override suspend fun deleteAll() = withContext(ioDispatcher) {
    TODO("Not yet implemented")
  }

  override suspend fun deleteNandaItem(uid: Long) = withContext(ioDispatcher) {
    TODO("Not yet implemented")
  }

  override suspend fun getFavoriteList(): List<NandaEntity> = withContext(ioDispatcher) {
    return@withContext db.dataDao.favorites()
  }

  override suspend fun getSearchList(keyword: String?): List<NandaEntity> = withContext(ioDispatcher) {
    return@withContext db.dataDao.search(keyword.orEmpty())
  }

  override suspend fun getHistories(): List<History> = withContext(ioDispatcher) {
    return@withContext db.historyDao.getAll().reversed()
  }

  override suspend fun saveHistory(keyword: String) = withContext(ioDispatcher) {
    db.historyDao.insertHistory(History(null, keyword))
  }

  override suspend fun deleteHistory(keyword: String) = withContext(ioDispatcher) {
    db.historyDao.delete(keyword)
  }
}
