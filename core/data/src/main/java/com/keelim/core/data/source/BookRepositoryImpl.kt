package com.keelim.core.data.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.keelim.core.data.paging.BookPagingSource
import com.keelim.core.network.TargetService
import com.keelim.core.network.model.Books
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookRepositoryImpl
@Inject
constructor(
    private val myGradeService: TargetService.MyGradeService,
) : BookRepository {

    override suspend fun getBooks(query: String, startIndex: Int, limit: Int): Books {
        return myGradeService.getBooks(query = query, startIndex = startIndex, limit = limit)
    }

    override fun getBookFlow(query: String): Flow<PagingData<Books.Book>> {
        return Pager(PagingConfig(pageSize = 10)) { BookPagingSource(query) }.flow
    }
}
