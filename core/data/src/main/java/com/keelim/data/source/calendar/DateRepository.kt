package com.keelim.data.source.calendar

data class EconomyCalendarDate(
    val date: String,
    val time: String,
    val country: String,
    val title: String,
    val priority: String,
)

interface DateRepository {
    suspend fun getEconomyCalendarDate(year: String, month: String): List<EconomyCalendarDate>
}
