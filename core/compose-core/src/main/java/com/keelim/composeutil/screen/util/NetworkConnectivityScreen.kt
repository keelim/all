package com.keelim.composeutil.screen.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.common.model.NetworkConnectivityService
import com.keelim.common.model.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
@Stable
@HiltViewModel
class NetworkViewModel @Inject constructor(
    networkConnectivityService: NetworkConnectivityService,
) : ViewModel() {
    val networkStatus: StateFlow<NetworkStatus> =
        networkConnectivityService.networkStatus.stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(),
            initialValue = NetworkStatus.Unknown,
        )
}

@Composable
fun NetworkConnectivityScreen(
    viewModel: NetworkViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
) {
    val networkStatus = viewModel.networkStatus.collectAsState()
    if (networkStatus.value == NetworkStatus.Disconnected) {
        LaunchedEffect(networkStatus) { snackbarHostState.showSnackbar("offline") }
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "Connectivity Se`rvice")
        }
    }
}
