package com.keelim.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationResponse(
    @SerialName("majorDimension")
    val majorDimension: String, // ROWS
    @SerialName("range")
    val range: String, // notifications!A1:Z1000
    @SerialName("values")
    val values: List<List<String>>,
)
