package com.keelim.cnubus.data.repository.setting

import com.keelim.cnubus.data.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUserInformation(): Flow<User>
    suspend fun setUserInformation(name:String)
}