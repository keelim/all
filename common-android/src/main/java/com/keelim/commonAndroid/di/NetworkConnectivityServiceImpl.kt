package com.keelim.commonAndroid.di

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.keelim.common.model.NetworkConnectivityService
import com.keelim.common.model.NetworkStatus
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class NetworkConnectivityServiceImpl @Inject constructor(context: Context) :
    NetworkConnectivityService {

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(ConnectivityManager::class.java)

    @SuppressLint("MissingPermission")
    override val networkStatus: Flow<NetworkStatus> = callbackFlow {
        val connectivityCallback =
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    trySend(NetworkStatus.Connected)
                }

                override fun onUnavailable() {
                    trySend(NetworkStatus.Disconnected)
                }

                override fun onLost(network: Network) {
                    trySend(NetworkStatus.Disconnected)
                }
            }

        val request =
            NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build()

        connectivityManager.registerNetworkCallback(request, connectivityCallback)

        awaitClose { connectivityManager.unregisterNetworkCallback(connectivityCallback) }
    }
}
