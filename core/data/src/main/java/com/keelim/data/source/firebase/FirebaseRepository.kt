package com.keelim.data.source.firebase

import com.keelim.data.model.EcoCalEntry
import kotlinx.coroutines.flow.Flow

interface FirebaseRepository {
    fun getRef(ref: String): Flow<Result<List<EcoCalEntry>>>
}
