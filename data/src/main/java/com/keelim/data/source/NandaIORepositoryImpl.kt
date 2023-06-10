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
package com.keelim.data.source

import com.keelim.data.db.NandaAppDatabase
import com.keelim.data.model.entity.NandaEntity
import com.keelim.data.model.entity.NandaEntity2
import com.keelim.data.di.IoDispatcher
import com.keelim.data.network.TargetService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class NandaIORepositoryImpl @Inject constructor(
    private val nandaService: TargetService.NandaService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val db: NandaAppDatabase,
) : NandaIORepository {
    override val searchData: Flow<List<NandaEntity>> =
        db.dataDao().getSearchData()
            .distinctUntilChanged()
            .flowOn(ioDispatcher)

    override suspend fun getNandaList(): List<NandaEntity2> = withContext(ioDispatcher) {
        val response = nandaService.getNandas()
        return@withContext if (response.isSuccessful) {
            response.body()?.items?.map { it.toEntity() } ?: listOf()
        } else {
            listOf()
        }
    }

    override suspend fun getFavoriteList(): List<NandaEntity> = withContext(ioDispatcher) {
        db.dataDao().favorites()
    }

    override suspend fun getSearchList(keyword: String?): List<NandaEntity> = withContext(ioDispatcher) {
        val result = db.dataDao().search(keyword.orEmpty())
        Timber.d("검색 결과", result)
        return@withContext result
    }

    override suspend fun saveHistory(keyword: String) = withContext(ioDispatcher) {
    }

    override suspend fun deleteHistory(keyword: String) = withContext(ioDispatcher) {
    }

    override suspend fun updateFavorite(favorite: Int, id: Int) = withContext(ioDispatcher) {
        db.dataDao().favoriteUpdate(favorite, id)
    }

    override fun getSearchFlow(query: String): Flow<List<NandaEntity>> {
        return db.dataDao().getSearchFlow(query)
    }
}
