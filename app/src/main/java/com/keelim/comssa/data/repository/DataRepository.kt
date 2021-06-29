package com.keelim.comssa.data.repository

import com.keelim.comssa.data.model.Data

interface DataRepository {

    suspend fun getAllDatas(): List<Data>
}