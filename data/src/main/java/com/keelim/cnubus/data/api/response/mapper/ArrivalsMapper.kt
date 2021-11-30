package com.keelim.cnubus.data.api.response.mapper

import com.keelim.cnubus.data.model.ArrivalInformation
import com.keelim.cnubus.data.model.Subway
import com.keelim.cnubus.data.api.response.RealtimeArrival
import java.text.SimpleDateFormat
import java.util.Locale

private val apiDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.'0'", Locale.KOREA)
private val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.KOREA)

private const val INVALID_FIELD = "-"

fun RealtimeArrival.toArrivalInformation(): ArrivalInformation =
    ArrivalInformation(
        subway = Subway.findById(subwayId),
        direction = trainLineNm?.split("-")
            ?.get(1)
            ?.trim()
            ?: INVALID_FIELD,
        destination = bstatnNm ?: INVALID_FIELD,
        message = arvlMsg2
            ?.replace(statnNm.toString(), "당역")
            ?.replace("[\\[\\]]".toRegex(), "")
            ?: INVALID_FIELD,
        updatedAt = recptnDt
            ?.let { apiDateFormat.parse(it) }
            ?.let { dateFormat.format(it) }
            ?: INVALID_FIELD
    )

fun List<RealtimeArrival>.toArrivalInformation(): List<ArrivalInformation> =
    map { it.toArrivalInformation() }
