package com.keelim.data.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.keelim.data.model.Books
import com.keelim.data.network.TargetService
import com.keelim.data.paging.BookPagingSource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

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
