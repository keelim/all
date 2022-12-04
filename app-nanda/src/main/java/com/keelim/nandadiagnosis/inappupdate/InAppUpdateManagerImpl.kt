package com.keelim.nandadiagnosis.inappupdate

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine
import timber.log.Timber

class InAppUpdateManagerImpl @Inject constructor(
    @ApplicationContext ctx: Context
) : InAppUpdateManager {

    private val appUpdateManager = AppUpdateManagerFactory.create(ctx)

    override suspend fun versionCode(): Int {
        return try {
            appUpdateManager.requestAppUpdateInfo()
                .availableVersionCode()
        } catch (e: Exception) {
            Timber.w(e)
            InAppUpdateManager.NOT_ALLOWED
        }
    }

    private suspend fun AppUpdateManager.requestAppUpdateInfo(): AppUpdateInfo {
        return suspendCoroutine { routine ->
            appUpdateInfo
                .addOnSuccessListener { routine.resumeWith(Result.success(it)) }
                .addOnFailureListener { routine.resumeWith(Result.failure(it)) }
        }
    }
}
