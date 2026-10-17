package com.keelim.appfunction.health

import androidx.appfunctions.AppFunctionContext
import androidx.appfunctions.service.AppFunction

class HealthAppFunctions {
    private val provider = HealthStatusProvider()

    /**
     * Returns a neutral health-check signal to validate App Functions integration.
     *
     * @param appFunctionContext The execution context provided by the AppFunction framework.
     * @return A [HealthStatus] object representing the health check status of the module.
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun getHealthStatus(appFunctionContext: AppFunctionContext): HealthStatus {
        return provider.status()
    }
}
