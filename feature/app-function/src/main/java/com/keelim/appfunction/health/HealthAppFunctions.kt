package com.keelim.appfunction.health

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appfunctions.AppFunctionDeclaration
import androidx.appfunctions.AppFunctionService
import androidx.appfunctions.AppFunctionServiceEntryPoint

@RequiresApi(Build.VERSION_CODES.BAKLAVA)
@AppFunctionServiceEntryPoint(
    serviceName = "GeneratedHealthAppFunctionService",
    appFunctionXmlFileName = "keelim_health_functions",
)
abstract class HealthAppFunctions : AppFunctionService(), HealthStatusSchema {
    private val provider = HealthStatusProvider()

    /**
     * Returns a neutral health-check signal to validate App Functions integration.
     */
    @AppFunctionDeclaration(isDescribedByKDoc = true)
    override suspend fun getHealthStatus(): HealthStatus {
        return provider.status()
    }
}
