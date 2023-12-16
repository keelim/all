package com.keelim.data.source.firebase

import com.google.firebase.Firebase
import com.google.firebase.database.database
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber

class FirebaseRepositoryImpl @Inject constructor() : FirebaseRepository {
  override fun getRef(ref: String): Flow<Result<EcocalEntries>> = callbackFlow {
      Timber.d("[lab] 요청 들어감")
      Firebase.database
          .getReference(ref)
          .get()
          .addOnSuccessListener { snapshot ->
              Timber.d("[lab] snapshot $snapshot")
              trySend(
                  runCatching { snapshot.getValue(EcocalEntries::class.java) ?: EcocalEntries(emptyList()) }
              )
          }
          .addOnFailureListener { error ->
              Timber.e(error.message)
              trySend(Result.failure(IllegalStateException("Firebase Error ${error.message}")))
          }
  }
}
