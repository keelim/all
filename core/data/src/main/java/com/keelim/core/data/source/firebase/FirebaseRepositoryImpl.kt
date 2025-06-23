package com.keelim.core.data.source.firebase

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.messaging.messaging
import com.google.firebase.remoteconfig.remoteConfig
import com.keelim.common.Dispatcher
import com.keelim.common.KeelimDispatchers
import com.keelim.data.repository.FirebaseRepository
import com.keelim.model.EcoCalEntry
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import timber.log.Timber
import javax.inject.Inject

class FirebaseRepositoryImpl
@Inject
constructor(
    @ApplicationContext val context: Context,
    @Dispatcher(KeelimDispatchers.IO) val dispatcher: CoroutineDispatcher,
) : FirebaseRepository {

    private val documentPath by lazy {
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).run {
            "%d-%02d".format(year, monthNumber)
        }
    }

    override fun getRef(ref: String): Flow<Result<List<EcoCalEntry>>> = flow {
        val refs = Firebase.firestore
            .collection(ref)
            .document(documentPath)
            .get()
            .await()
            .let { document ->
                val size = document.get("size", Int::class.java) ?: 0
                val fields = mutableListOf<EcoCalEntry>()
                for (index in 0 until size) {
                    document.get("$index", EcoCalEntry::class.java)
                        ?.also(fields::add)
                }
                fields
            }

        emit(Result.success(refs))
    }.catch { throwable ->
        Timber.e(throwable)
        emit(Result.failure(throwable))
    }

    override fun getFCMToken(): Flow<Result<String>> = flow {
        emit(Result.success(Firebase.messaging.token.await()))
    }.catch { throwable ->
        Timber.e(throwable)
        emit(Result.failure(throwable))
    }

    override fun getValue(key: String): String {
        return runCatching {
            Firebase.remoteConfig.getString(key)
        }.getOrDefault("")
    }
}
