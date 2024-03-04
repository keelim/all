package com.keelim.core.data.source.notification

import com.keelim.common.di.IoDispatcher
import com.keelim.core.data.BuildConfig
import com.keelim.core.network.di.KtorNetworkModule
import com.keelim.core.network.model.CommonResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class NotificationRepositoryImpl
@Inject
constructor(
    @KtorNetworkModule.KtorAndroidClient val client: HttpClient,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : NotificationRepository {
    override suspend fun getNotification(): List<Notification> {
        return withContext(dispatcher) {
            client
                .use<HttpClient, CommonResponse> {
                    it.get {
                        url("${BuildConfig.NOTIFICATION_URL}/notifications")
                        parameter("key", BuildConfig.SHEET_KEY)
                    }
                        .body()
                }
                .values
                .map { (date, title, desc) ->
                    Notification(
                        date = date,
                        title = title,
                        desc = desc
                    )
                }
        }
    }
}
