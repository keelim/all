package com.keelim.appfunction.health

import androidx.appfunctions.AppFunctionSchemaDefinition

@AppFunctionSchemaDefinition(
    category = "com.keelim.appfunction.health",
    name = "getHealthStatus",
    version = 1,
)
interface HealthStatusSchema {
    suspend fun getHealthStatus(): HealthStatus
}
