package com.keelim.core.data.source

import com.keelim.core.data.repository.Base64Repository
import com.keelim.shared.data.database.dao.Base64Dao
import com.keelim.shared.data.database.model.Base64History
import kotlinx.coroutines.flow.Flow
import jakarta.inject.Inject

class Base64RepositoryImpl @Inject constructor(
    private val base64Dao: Base64Dao,
) : Base64Repository {
    override fun getAllHistory(): Flow<List<Base64History>> = base64Dao.getAll()

    override suspend fun insertHistory(text: String, isEncoded: Boolean) {
        base64Dao.insert(
            Base64History(
                text = text,
                isEncoded = isEncoded,
            )
        )
    }

    override suspend fun deleteHistory(history: Base64History) {
        base64Dao.delete(history)
    }

    override suspend fun deleteAllHistory() {
        base64Dao.deleteAll()
    }
}
