package com.keelim.common.model

import kotlinx.coroutines.flow.Flow

sealed interface NetworkStatus {
    data object Unknown : NetworkStatus
    data object Connected : NetworkStatus
    data object Disconnected : NetworkStatus
}

interface NetworkConnectivityService {
    val networkStatus: Flow<NetworkStatus>
}
