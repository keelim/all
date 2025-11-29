package com.keelim.data.repository

import com.keelim.core.model.NandaDiagnosis
import kotlinx.coroutines.flow.Flow

interface NandaRepository {
    val nandaDiagnosis: Flow<List<NandaDiagnosis>>
    fun getDiagnosis(query: String): Flow<List<NandaDiagnosis>>
}
