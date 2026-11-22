package com.keelim.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MarketSchedule(
    val id: String,
    val name: String,
    val hour: Int,
    val minute: Int,
    val isEnabled: Boolean = true,
    val isDefault: Boolean = false
) {
    companion object {
        val KOREA_MARKET = MarketSchedule(
            id = "korea_market",
            name = "Korea (KOSPI/KOSDAQ)",
            hour = 9,
            minute = 0,
            isEnabled = false,
            isDefault = true
        )
        
        val US_MARKET_WINTER = MarketSchedule(
            id = "us_market",
            name = "US (NYSE/NASDAQ)",
            hour = 23,
            minute = 30,
            isEnabled = false,
            isDefault = true
        )
    }
}
