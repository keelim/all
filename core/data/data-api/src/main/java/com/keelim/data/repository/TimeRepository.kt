package com.keelim.data.repository

interface TimeRepository {
    suspend fun getCurrentTime(): Long
}
