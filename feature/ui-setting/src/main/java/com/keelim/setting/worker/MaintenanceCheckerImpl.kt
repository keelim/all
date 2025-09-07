package com.keelim.setting.worker

import android.content.Context
import android.content.Intent
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.keelim.common.Dispatcher
import com.keelim.common.KeelimDispatchers
import com.keelim.data.repository.NotificationRepository
import com.keelim.domain.MaintenanceChecker
import com.keelim.setting.screen.maintenance.MaintenanceActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MaintenanceCheckerImpl
@Inject
constructor(
    @ApplicationContext private val context: Context,
    private val notificationRepository: NotificationRepository,
    @Dispatcher(KeelimDispatchers.DEFAULT) private val dispatcher: CoroutineDispatcher,
) : MaintenanceChecker, DefaultLifecycleObserver {
    private val _isUnderMaintenance = MutableStateFlow(false)
    override val isUnderMaintenance: StateFlow<Boolean> = _isUnderMaintenance

    private val scope = CoroutineScope(dispatcher + SupervisorJob())
    private var previousValue = false

    override fun onStart(owner: LifecycleOwner) {
        scope.launch {
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
                if (isUnderMaintenance && !previousValue) {
                    context.startActivity(
                        Intent(context, MaintenanceActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        },
                    )
                }
                previousValue = isUnderMaintenance
            }
            .launchIn(scope)
    }

    override fun onStop(owner: LifecycleOwner) {
        scope.coroutineContext.cancelChildren()
    }
}
