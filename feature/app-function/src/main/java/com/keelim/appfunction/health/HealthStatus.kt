package com.keelim.appfunction.health

import androidx.appfunctions.AppFunctionSerializable

@AppFunctionSerializable
data class HealthStatus(
    val status: String,
    val module: String,
    val epochMillis: Long,
)
