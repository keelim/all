package com.keelim.core.data.source.firebase

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.database.Logger
import com.google.firebase.database.database
import com.google.firebase.messaging.messaging
import com.keelim.core.data.BuildConfig
import com.keelim.core.network.Dispatcher
import com.keelim.core.network.KeelimDispatchers
import com.keelim.data.repository.FirebaseRepository
import com.keelim.model.EcoCalEntry
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class FirebaseRepositoryImpl
@Inject
constructor(
    @ApplicationContext val context: Context,
    @Dispatcher(KeelimDispatchers.IO) val dispatcher: CoroutineDispatcher,
) : FirebaseRepository {

    private val firebase by lazy {
        FirebaseApp.initializeApp(context)
        Firebase
    }

    override fun getRef(ref: String): Flow<Result<List<EcoCalEntry>>> = flow {
        val database =
            firebase.database.apply {
                if (BuildConfig.DEBUG) {
                    setLogLevel(Logger.Level.DEBUG)
                }
                setPersistenceEnabled(true)
            }

        val refs = database
            .getReference(ref)
            .get()
            .await()
            .takeIf { it.exists() }
            ?.children
            ?.mapNotNull {
                it.getValue(EcoCalEntry::class.java)
            } ?: emptyList()

        emit(Result.success(refs))
    }.catch { throwable ->
        Timber.e(throwable)
        emit(Result.failure(throwable))
    }

    override fun getFCMToken(): Flow<Result<String>> = flow {
        emit(Result.success(firebase.messaging.token.await()))
    }.catch { throwable ->
        Timber.e(throwable)
        emit(Result.failure(throwable))
    }
}
