package com.keelim.setting.worker

import android.app.Application
import android.content.Intent
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.keelim.common.di.ApplicationScope
import com.keelim.data.repository.NotificationRepository
import com.keelim.domain.MaintenanceChecker
import com.keelim.setting.screen.maintenance.MaintenanceActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import jakarta.inject.Inject

class MaintenanceCheckerImpl
@Inject
constructor(
    private val application: Application,
    private val notificationRepository: NotificationRepository,
    @ApplicationScope
    private val applicationScope: CoroutineScope,
) : MaintenanceChecker {
    private val _isUnderMaintenance = MutableStateFlow(false)
    override val isUnderMaintenance: StateFlow<Boolean> = _isUnderMaintenance
    private var previousValue = false

    override fun initialize() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onStart(owner: LifecycleOwner) {
        applicationScope.launch {
            while (true) {
                try {
                    val notifications = notificationRepository.getNotification()
                    _isUnderMaintenance.value = notifications.any { it.fixed }
                } catch (e: Exception) {
                    // Handle exception
                }
                delay(60_000)
            }
        }
        isUnderMaintenance
            .onEach { isUnderMaintenance ->
                if (isUnderMaintenance && previousValue.not()) {
                    application.startActivity(
                        Intent(application, MaintenanceActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        },
                    )
                }
                previousValue = isUnderMaintenance
            }
            .launchIn(applicationScope)
    }

    override fun onStop(owner: LifecycleOwner) {
        applicationScope.cancel()
    }
}
