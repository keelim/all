package com.keelim.appfunction.health

import androidx.appfunctions.AppFunctionContext
import androidx.appfunctions.service.AppFunction

class HealthAppFunctions {
    private val provider = HealthStatusProvider()

    /**
     * Returns a neutral health-check signal to validate App Functions integration.
     */
    @AppFunction(isDescribedByKdoc = true)
    suspend fun getHealthStatus(appFunctionContext: AppFunctionContext): HealthStatus {
        return provider.status()
    }
}
