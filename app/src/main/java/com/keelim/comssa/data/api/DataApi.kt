package com.keelim.comssa.data.api

import com.keelim.comssa.data.model.Data

interface DataApi {
    suspend fun getAllData():List<Data>

    suspend fun getDatas(dataIds:List<String>): List<Data>

}