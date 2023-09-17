package com.keelim.domain

import com.keelim.data.source.local.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class ShowEventUseCase @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {
    operator fun invoke(): Flow<Boolean> = dataStoreManager.getValue<Boolean>("donotshow")
        .mapLatest { value -> (value == null || value==true).not() }
}
