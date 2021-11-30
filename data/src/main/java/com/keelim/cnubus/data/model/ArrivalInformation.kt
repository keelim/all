package com.keelim.cnubus.data.model

data class ArrivalInformation(
    val subway: Subway,
    val direction: String,
    val message: String,
    val destination: String,
    val updatedAt: String
)
