package com.keelim.data.source.calendar

import com.keelim.data.BuildConfig
import com.keelim.data.di.IoDispatcher
import com.keelim.data.di.network.KtorNetworkModule
import com.keelim.data.network.response.CommonResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DateRepositoryImpl @Inject constructor(
    @KtorNetworkModule.KtorAndroidClient val client: HttpClient,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : DateRepository {
    override suspend fun getEconomyCalendarDate(
        year: String,
        month: String,
    ): List<EconomyCalendarDate> =
        withContext(dispatcher) {
            client
                .use<HttpClient, CommonResponse> {
                    it.get {
                        url("${BuildConfig.NOTIFICATION_URL}/calendar-$year-$month")
                        parameter("key", BuildConfig.SHEET_KEY)
                    }.body()
                }
                .values
                .drop(1)
                .map { (date, time, country, title, priority) ->
                    EconomyCalendarDate(
                        date = date,
                        time = time,
                        country = country,
                        title = title,
                        priority = priority,
                    )
                }
        }
}
