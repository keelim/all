package com.keelim.comssa.data.repository

import com.keelim.comssa.data.api.DataApi
import com.keelim.comssa.data.model.Data
import com.keelim.comssa.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(
    private val dataApi:DataApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : DataRepository {
    override suspend fun getAllDatas(): List<Data>  = withContext(ioDispatcher){
        return@withContext dataApi.getAllData()
    }
}