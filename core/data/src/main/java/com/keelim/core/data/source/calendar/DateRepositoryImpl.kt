package com.keelim.core.data.source.calendar

import com.keelim.common.di.IoDispatcher
import com.keelim.core.data.BuildConfig
import com.keelim.core.network.di.KtorNetworkModule
import com.keelim.core.network.model.CommonResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

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
