package com.keelim.commonAndroid.core

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Stable
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.keelim.common.extensions.toast
import com.keelim.common.model.NetworkConnectivityService
import com.keelim.common.model.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
@Stable
@HiltViewModel
class AppMainViewModel @Inject constructor(
    val networkConnectivityService: NetworkConnectivityService,
    val errorDelegate: ErrorDelegate,
) : ViewModel()

class AppMainDelegator(
    private val activity: ComponentActivity,
    private val viewModel: AppMainViewModel,
) : DefaultLifecycleObserver {

    init {
        activity.lifecycle.addObserver(
            this,
        )
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)

        with(activity) {
            viewModel.networkConnectivityService
                .networkStatus
                .flowWithLifecycle(activity.lifecycle)
                .onEach { state ->
                    if (state == NetworkStatus.Disconnected) {
                        activity.toast("네트워크를 확인하고 있습니다. ")
                    }
                }.launchIn(activity.lifecycleScope)
        }
    }
}
