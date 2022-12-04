package com.keelim.nandadiagnosis.inappupdate

interface InAppUpdateManager {
    suspend fun versionCode(): Int
    companion object {
        const val NOT_ALLOWED = -1
    }
}
