package com.keelim.cnubus.data.remote

interface RemoteDataSource {
    suspend fun getRoadInformation(type:String): List<String>
}