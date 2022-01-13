package com.keelim.data.api.response


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class ResponseNotification(
    @Json(name = "majorDimension")
    var majorDimension: String?, // ROWS
    @Json(name = "range")
    var range: String?, // main!A1:Z1000
    @Json(name = "values")
    var values: List<List<String>>
) : Parcelable