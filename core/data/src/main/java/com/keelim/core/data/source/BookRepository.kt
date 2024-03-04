package com.keelim.core.data.source

import androidx.paging.PagingData
import com.keelim.core.network.model.Books
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun getBooks(query: String, startIndex: Int, limit: Int): Books
    fun getBookFlow(query: String): Flow<PagingData<Books.Book>>
}
