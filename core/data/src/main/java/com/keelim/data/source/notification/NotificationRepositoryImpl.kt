package com.keelim.data.source.notification

import com.keelim.data.BuildConfig
import com.keelim.data.di.IoDispatcher
import com.keelim.data.di.network.KtorNetworkModule
import com.keelim.data.network.response.NotificationResponse
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
          .use<HttpClient, NotificationResponse> {
            it.get {
                  url("${BuildConfig.NOTIFICATION_URL}/notifications")
                  parameter("key", BuildConfig.SHEET_KEY)
                }
                .body()
          }
          .values
          .map { (date, title, desc) -> Notification(date = date, title = title, desc = desc) }
    }
  }
}
