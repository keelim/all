package com.keelim.data.source.notification

import com.keelim.data.BuildConfig
import com.keelim.data.di.IoDispatcher
import com.keelim.data.di.network.KtorNetworkModule
import com.keelim.data.network.response.NotificationResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    @KtorNetworkModule.KtorAndroidClient val client: HttpClient,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : NotificationRepository {
    override suspend fun getNotification(): List<Notification> {
        return withContext(dispatcher) {
            val response: NotificationResponse = client.get(BuildConfig.NOTIFICATION_URL).body()
            response.values.map { value ->
                Notification(date = value[0], title = value[1], desc = value[2])
            }
        }
    }
}
