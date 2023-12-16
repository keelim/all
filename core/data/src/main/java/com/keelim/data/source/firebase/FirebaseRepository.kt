package com.keelim.data.source.firebase

import kotlinx.coroutines.flow.Flow

data class EcocalEntries(
    val entries: List<EcoCalEntry>
)
data class EcoCalEntry(
    val country: String,
    val date: String,
    val priority: String,
    val time: String,
    val title: String,
)
interface FirebaseRepository {
    fun getRef(ref: String): Flow<Result<EcocalEntries>>
}
