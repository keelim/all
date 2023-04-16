package com.keelim.data.repository.mygrade

import androidx.paging.PagingData
import com.keelim.data.api.response.mygrade.Books
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun getBooks(query: String, startIndex: Int, limit: Int): Books
    fun getBookFlow(query: String): Flow<PagingData<Books.Book>>
}
