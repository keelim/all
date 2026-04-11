package com.keelim.appfunction.health

import androidx.appfunctions.AppFunctionContext
import androidx.appfunctions.service.AppFunction

class HealthAppFunctions {
    /**
     * Returns a neutral health-check signal to validate App Functions integration.
     */
    @AppFunction(isDescribedByKdoc = true)
    suspend fun getHealthStatus(appFunctionContext: AppFunctionContext): HealthStatus {
        return HealthStatus(
            status = "ok",
            module = "feature:app-function",
            epochMillis = System.currentTimeMillis(),
        )
    }
}
