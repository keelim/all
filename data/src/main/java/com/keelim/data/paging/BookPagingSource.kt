package com.keelim.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.keelim.common.extensions.nextKey
import com.keelim.common.extensions.prevKey
import com.keelim.data.model.Books
import com.keelim.data.network.TargetService
import javax.inject.Inject

class BookPagingSource(
    private val query: String
) : PagingSource<Int, Books.Book>() {

    @Inject
    lateinit var bookService: TargetService.MyGradeService
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Books.Book> {
        return try {
            val nextPage = params.key ?: 1
            val books = bookService.getBooks(query, nextPage, params.loadSize)
            LoadResult.Page(
                data = books.items,
                prevKey = params.prevKey(),
                nextKey = params.nextKey(books.totalItems)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Books.Book>): Int =
        ((state.anchorPosition ?: 0) - state.config.initialLoadSize / 2).coerceAtLeast(0)
}
