package com.keelim.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.keelim.core.database.dao.HistoryDao
import com.keelim.core.database.model.History
import javax.inject.Inject

class DBPagingSource @Inject constructor(
    private val historyDao: HistoryDao,
) : PagingSource<Int, History>() {
    override fun getRefreshKey(state: PagingState<Int, History>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, History> {
        val page = params.key ?: 1
        return try {
            val items = historyDao.getPagingAll(page, params.loadSize)
            LoadResult.Page(
                data = items,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (items.isEmpty()) null else page + (params.loadSize / 10),
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}
