package com.keelim.comssa.data.repository

import com.keelim.comssa.data.api.UserApi
import com.keelim.comssa.data.model.User
import com.keelim.comssa.data.preference.PreferenceManager
import com.keelim.comssa.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val preferenceManager: PreferenceManager,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
): UserRepository{
    override suspend fun getUser(): User? {
        return preferenceManager.getString(KEY_USER_ID)?.let {
            User(it)
        }
    }

    override suspend fun saveUser(user: User) {
        val newvie = userApi.saveUser(user)
        preferenceManager.putString(KEY_USER_ID, newvie.id!!)
    }

    companion object{
        private const val KEY_USER_ID = "KEY_USER_ID"
    }

}