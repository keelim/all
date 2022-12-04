package com.keelim.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class ResponseNotification(
    var majorDimension: String?, // ROWS
    var range: String?, // main!A1:Z1000
    var values: List<List<String>>,
) : Parcelable
