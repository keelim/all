package com.keelim.setting.worker

import android.app.Application
import android.content.Intent
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.keelim.common.di.ApplicationScope
import com.keelim.data.repository.NotificationRepository
import com.keelim.domain.MaintenanceChecker
import com.keelim.setting.screen.maintenance.MaintenanceActivity
import jakarta.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber

class MaintenanceCheckerImpl
@Inject
constructor(
    private val application: Application,
    private val notificationRepository: NotificationRepository,
    @ApplicationScope
    private val applicationScope: CoroutineScope,
) : MaintenanceChecker, DefaultLifecycleObserver {
    private val _isUnderMaintenance = MutableStateFlow(false)
    override val isUnderMaintenance: StateFlow<Boolean> = _isUnderMaintenance
    private var previousValue = false
    private var pollingJob: Job? = null
    private var observerJob: Job? = null

    override fun initialize() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onStart(owner: LifecycleOwner) {
        if (pollingJob?.isActive != true) {
            pollingJob = applicationScope.launch {
                while (isActive) {
                    try {
                        val notifications = notificationRepository.getNotification()
                        _isUnderMaintenance.value = notifications.any { it.fixed }
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        Timber.e(e, "Failed to refresh maintenance status")
                    }
                    delay(60_000)
                }
            }
        }

        if (observerJob?.isActive != true) {
            observerJob = isUnderMaintenance
                .onEach { underMaintenance ->
                    if (underMaintenance && previousValue.not()) {
                        application.startActivity(
                            Intent(application, MaintenanceActivity::class.java).apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            },
                        )
                    }
                    previousValue = underMaintenance
                }
                .launchIn(applicationScope)
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        pollingJob?.cancel()
        pollingJob = null
        observerJob?.cancel()
        observerJob = null
    }
}
