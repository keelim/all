package com.keelim.data.repository

import com.keelim.model.EcoCalEntry
import kotlinx.coroutines.flow.Flow

interface FirebaseRepository {
    fun getRef(ref: String): Flow<Result<List<EcoCalEntry>>>
}
