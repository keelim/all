package com.keelim.cnubus.data.repository.setting

import com.keelim.cnubus.data.db.DataStoreManager
import com.keelim.cnubus.data.model.User
import com.keelim.cnubus.di.IoDispatcher
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

internal class UserRepositoryImpl @Inject constructor(
    @IoDispatcher val io: CoroutineDispatcher,
    private val dataStoreManager: DataStoreManager,
) : UserRepository {
    override suspend fun getUserInformation(): Flow<User> = withContext(io) {
        flow {
            val id = dataStoreManager.getUserId("id")
            id.collect { userId ->
                emit(
                    User(
                        id = userId
                    )
                )
            }
        }
    }
}