package com.keelim.comssa.data.repository

import com.keelim.comssa.data.model.User

interface UserRepository {
    suspend fun getUser():User?

    suspend fun saveUser(user: User)
}