package com.keelim.comssa.data.repository

import com.keelim.comssa.data.model.Data

interface DataRepository {

    suspend fun getAllDatas(): List<Data>

    suspend fun getData(dataIds: List<String>): List<Data>
}