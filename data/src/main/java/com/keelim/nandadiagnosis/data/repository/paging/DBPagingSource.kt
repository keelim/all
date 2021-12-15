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
package com.keelim.nandadiagnosis.data.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.keelim.nandadiagnosis.data.db.dao.DataDaoV2
import com.keelim.nandadiagnosis.data.db.entity.NandaEntity
import javax.inject.Inject

class DBPagingSource @Inject constructor(
  private val dao: DataDaoV2,
  private val query: String,
) : PagingSource<Int, NandaEntity>() {

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NandaEntity> {
    val page = params.key ?: 1
    return try {
      val items = dao.getQueryContentsByPaging(query, page, params.loadSize)
      LoadResult.Page(
        data = items,
        prevKey = if (page == 1) null else page - 1,
        nextKey = if (items.isEmpty()) null else page + (params.loadSize / 10)
      )
    } catch (e: Exception) {
      return LoadResult.Error(e)
    }
  }

  override fun getRefreshKey(state: PagingState<Int, NandaEntity>): Int? {
    return state.anchorPosition?.let { anchorPosition ->
      state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
        ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
    }
  }
}
