package com.keelim.comssa.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.keelim.comssa.data.db.dao.SearchDao
import com.keelim.comssa.data.db.entity.Search

class FavoritePagingSource(
    private val dao: SearchDao,
): PagingSource<Int, Search>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Search> {
        val page = params.key ?: 1
        return try {
            val items = dao.getFavoriteByPaging(page, params.loadSize)
            LoadResult.Page(
                data = items,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (items.isEmpty()) null else page + (params.loadSize / 10)
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
    
    override fun getRefreshKey(state: PagingState<Int, Search>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}