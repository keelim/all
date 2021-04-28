package com.keelim.cnubus.data.repository

interface Repository {
    suspend fun getRoadInformation(type: String): List<String>
}