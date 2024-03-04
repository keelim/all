package com.keelim.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommonResponse(
    @SerialName("majorDimension")
    val majorDimension: String, // ROWS
    @SerialName("range")
    val range: String, // notifications!A1:Z1000
    @SerialName("values")
    val values: List<List<String>>,
)
