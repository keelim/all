package com.keelim.domain

import com.keelim.core.data.source.local.DataStoreManager
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class ShowEventUseCase @Inject constructor(
    private val dataStoreManager: DataStoreManager,
) {
    operator fun invoke(): Flow<Boolean> = dataStoreManager.getValue<Boolean>("donotshow")
        .mapLatest { value -> (value == null || value == true).not() }
}
