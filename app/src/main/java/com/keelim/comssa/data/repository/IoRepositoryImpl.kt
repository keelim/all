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
package com.keelim.comssa.data.repository

import com.keelim.comssa.data.db.AppDatabase
import com.keelim.comssa.data.db.entity.Search
import com.keelim.comssa.di.IoDispatcher
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class IoRepositoryImpl @Inject constructor(
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
  private val db: AppDatabase,
) : IoRepository {
  override suspend fun getSearch(keyword: String): List<Search> = withContext(ioDispatcher) {
    return@withContext db.searchDao.getSearch(keyword)
  }

  override suspend fun updateFavorite(favorite: Int, id: Int) = withContext(ioDispatcher) {
    return@withContext db.searchDao.favoriteUpdate(favorite, id)
  }

  override suspend fun getFavorite(): List<Search> = withContext(ioDispatcher) {
    return@withContext db.searchDao.getFavorite()
  }

}
