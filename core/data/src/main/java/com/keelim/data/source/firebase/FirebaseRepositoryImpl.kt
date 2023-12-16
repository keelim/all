package com.keelim.data.source.firebase

import com.google.firebase.Firebase
import com.google.firebase.database.Logger
import com.google.firebase.database.database
import com.keelim.data.BuildConfig
import com.keelim.data.di.IoDispatcher
import com.keelim.data.model.EcoCalEntry
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

class FirebaseRepositoryImpl
@Inject
constructor(@IoDispatcher val dispatcher: CoroutineDispatcher) : FirebaseRepository {
  override fun getRef(ref: String): Flow<Result<List<EcoCalEntry>>> = callbackFlow {
    val database =
        Firebase.database.apply {
          if (BuildConfig.DEBUG) {
            setLogLevel(Logger.Level.DEBUG)
          }
          setPersistenceEnabled(true)
        }
    trySend(
        runCatching {
              withContext(dispatcher) {
                database
                    .getReference(ref)
                    .get()
                    .await()
                    .takeIf { it.exists() }
                    ?.children
                    ?.mapNotNull { it.getValue(EcoCalEntry::class.java) } ?: emptyList()
              }
            }
            .onFailure { throwable ->
              Timber.e(throwable)
              throwable.message
            })
  }
}
