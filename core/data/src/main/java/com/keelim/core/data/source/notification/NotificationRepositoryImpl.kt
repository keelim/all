package com.keelim.core.data.source.notification

import com.keelim.common.di.IoDispatcher
import com.keelim.core.data.BuildConfig
import com.keelim.core.network.di.KtorNetworkModule
import com.keelim.core.network.model.NoticeResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.url
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

class NotificationRepositoryImpl
@Inject
constructor(
    @KtorNetworkModule.KtorAndroidClient val client: HttpClient,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : NotificationRepository {
    override suspend fun getNotification(): List<Notification> {
        return withContext(dispatcher) {
            client
                .use<HttpClient, List<NoticeResponse>> {
                    it.get {
                        url("${BuildConfig.NOTIFICATION_URL}/rest/v1/notice")
                        headers {
                            append("apikey", BuildConfig.SHEET_KEY)
                            append("Authorization", "Bearer ${BuildConfig.SHEET_KEY}")
                        }
                    }.body()
                }
                .map {
                    val localDate = it.createdAt.toLocalDateTime(TimeZone.UTC)
                    val formattedDate = String.format("%d-%02d-%02d", localDate.year, localDate.month.value, localDate.dayOfMonth)
                    Notification(
                        date = formattedDate,
                        title = it.title,
                        desc = it.description,
                        fixed = it.fixed,
                    )
                }
        }
    }
}
