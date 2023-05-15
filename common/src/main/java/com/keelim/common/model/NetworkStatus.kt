package com.keelim.common.model

import kotlinx.coroutines.flow.Flow

sealed class NetworkStatus {
    object Unknown: NetworkStatus()
    object Connected: NetworkStatus()
    object Disconnected: NetworkStatus()
}

interface NetworkConnectivityService {
    val networkStatus: Flow<NetworkStatus>
}
