package com.keelim.comssa.data.api

import com.keelim.comssa.data.model.User

interface UserApi {
    suspend fun saveUser(user: User):User
}