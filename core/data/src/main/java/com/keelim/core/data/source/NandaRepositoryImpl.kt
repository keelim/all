package com.keelim.core.data.source

import com.keelim.core.model.NandaDiagnosis
import com.keelim.data.repository.NandaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.keelim.shared.data.database.dao.NandaDao
import com.keelim.shared.data.database.model.NandaEntity

class NandaRepositoryImpl @Inject constructor(
    private val nandaDao: NandaDao,
) : NandaRepository {
    override val nandaDiagnosis: Flow<List<NandaDiagnosis>> = nandaDao.getNandaEntities()
        .map { items ->
            items.map { it.toModel() }
        }

    override fun getDiagnosis(query: String): Flow<List<NandaDiagnosis>> = nandaDao.getDiagnosis(query)
        .map { items ->
            items.map { it.toModel() }
        }
}

private fun NandaEntity.toModel() = NandaDiagnosis(
    reason = reason,
    domain = domain_name,
    className = class_name,
    definition = diagnosis,
)
