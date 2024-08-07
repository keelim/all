package com.keelim.core.data.source.firebase

import com.google.firebase.Firebase
import com.google.firebase.database.Logger
import com.google.firebase.database.database
import com.keelim.common.di.IoDispatcher
import com.keelim.core.data.BuildConfig
import com.keelim.data.repository.FirebaseRepository
import com.keelim.model.EcoCalEntry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class FirebaseRepositoryImpl
@Inject
constructor(
    @IoDispatcher val dispatcher: CoroutineDispatcher,
) : FirebaseRepository {
    override fun getRef(ref: String): Flow<Result<List<EcoCalEntry>>> = flow {
        val database =
            Firebase.database.apply {
                if (BuildConfig.DEBUG) {
                    setLogLevel(Logger.Level.DEBUG)
                }
                setPersistenceEnabled(true)
            }

        emit(
            runCatching {
                withContext(dispatcher) {
                    database
                        .getReference(ref)
                        .get()
                        .await()
                        .takeIf { it.exists() }
                        ?.children
                        ?.mapNotNull {
                            it.getValue(EcoCalEntry::class.java)
                        } ?: emptyList()
                }
            }
                .onFailure { throwable ->
                    Timber.e(throwable)
                    throwable.message
                },
        )
    }
}
