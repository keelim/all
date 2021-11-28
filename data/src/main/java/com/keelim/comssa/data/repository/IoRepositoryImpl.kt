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

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.keelim.comssa.data.db.AppDatabase
import com.keelim.comssa.data.db.entity.Search
import com.keelim.comssa.data.paging.FavoritePagingSource
import com.keelim.comssa.data.paging.SearchPagingSource
import com.keelim.comssa.di.IoDispatcher
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
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

  override val favoriteFlow: Flow<List<Search>>
    get() = db.searchDao.getFavorite2()

  override fun getContentItemsByPaging(query:String): Flow<PagingData<Search>> {
    return Pager(
      config = PagingConfig(pageSize = 10),
      pagingSourceFactory = { SearchPagingSource(db.searchDao, query) }
    ).flow
  }

  override fun getFavoriteItemsByPaging(): Flow<PagingData<Search>> {
    return Pager(
      config = PagingConfig(pageSize = 10),
      pagingSourceFactory = { FavoritePagingSource(db.searchDao) }
    ).flow
  }
}
