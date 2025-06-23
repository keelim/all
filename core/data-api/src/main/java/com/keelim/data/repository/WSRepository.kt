package com.keelim.data.repository

interface WSRepository {
    suspend fun connect(host: String)
    suspend fun disconnect()
}
