package com.keelim.comssa.data.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    fun getValue(key: String, defaultValue: String): Flow<String>
    suspend fun setValue(key: String, value: String)
}
