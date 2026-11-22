package com.keelim.appfunction.health

import androidx.annotation.RequiresApi
import androidx.appfunctions.AppFunctionDeclaration
import androidx.appfunctions.AppFunctionService
import androidx.appfunctions.AppFunctionServiceEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** Neutral integration health signal; no personal health information is read or returned. */
@RequiresApi(36)
@AppFunctionServiceEntryPoint(
    serviceName = "HealthAppFunctionService",
    appFunctionXmlFileName = "health_app_functions",
)
abstract class HealthAppFunctions : AppFunctionService() {
    private val provider = HealthStatusProvider()

    /**
     * Returns a neutral health-check signal to validate App Functions integration.
     *
     * @return A [HealthStatus] object representing the health check status of the module.
     */
    @AppFunctionDeclaration(isDescribedByKDoc = true)
    suspend fun getHealthStatus(): HealthStatus = withContext(Dispatchers.Default) {
        provider.status()
    }
}
