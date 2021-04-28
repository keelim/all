package com.keelim.cnubus.data.repository

import com.keelim.cnubus.data.remote.RemoteDataSource
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
): Repository {
    override suspend fun getRoadInformation(type: String): List<String> {
        return remoteDataSource.getRoadInformation(type)
    }
}