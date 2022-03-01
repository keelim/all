package com.keelim.cnubus.domain

import com.keelim.cnubus.data.model.User
import com.keelim.cnubus.data.repository.setting.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

class UserUseCase(
    private val userRepository: UserRepository,
) {
    suspend fun getUserName(): Flow<User> = userRepository
        .getUserInformation()
        .distinctUntilChanged()
}