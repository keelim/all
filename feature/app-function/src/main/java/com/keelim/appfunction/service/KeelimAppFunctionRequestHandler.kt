package com.keelim.appfunction.service

import androidx.appfunctions.AppFunctionData
import androidx.appfunctions.AppFunctionFunctionNotFoundException
import androidx.appfunctions.AppFunctionInvalidArgumentException
import androidx.appfunctions.ExecuteAppFunctionRequest
import androidx.appfunctions.ExecuteAppFunctionResponse
import androidx.appfunctions.metadata.AppFunctionComponentsMetadata
import androidx.appfunctions.metadata.AppFunctionDataTypeMetadata
import androidx.appfunctions.metadata.AppFunctionMetadata
import androidx.appfunctions.metadata.AppFunctionObjectTypeMetadata
import androidx.appfunctions.metadata.AppFunctionReferenceTypeMetadata
import com.keelim.appfunction.health.HealthStatusProvider
import com.keelim.appfunction.json.JsonFormatterFunctionHandler

/** Uses public metadata-aware builders so the wire types match the compiler's XML. */
internal class KeelimAppFunctionRequestHandler(
    private val health: HealthStatusProvider = HealthStatusProvider(),
    private val json: JsonFormatterFunctionHandler = JsonFormatterFunctionHandler(),
) {
    suspend fun execute(
        request: ExecuteAppFunctionRequest,
        metadata: AppFunctionMetadata,
    ): ExecuteAppFunctionResponse {
        val components = metadata.packageMetadata.components
        val result = AppFunctionData.Builder(metadata.response, components)
        when (request.functionIdentifier) {
            HEALTH_ID -> result.setAppFunctionData(
                ExecuteAppFunctionResponse.Success.PROPERTY_RETURN_VALUE,
                AppFunctionData.serialize(
                    resolveObject(metadata.response.valueType, components),
                    components,
                    health.status(),
                ),
            )
            JSON_ID -> result.setString(
                ExecuteAppFunctionResponse.Success.PROPERTY_RETURN_VALUE,
                json.formatJson(
                    request.functionParameters.getString("inputJson")
                        ?: throw AppFunctionInvalidArgumentException("Missing inputJson parameter"),
                ),
            )
            else -> throw AppFunctionFunctionNotFoundException(
                "${request.functionIdentifier} is not available",
            )
        }
        return ExecuteAppFunctionResponse.Success(result.build())
    }

    private fun resolveObject(
        valueType: AppFunctionDataTypeMetadata,
        components: AppFunctionComponentsMetadata,
    ): AppFunctionObjectTypeMetadata {
        var current = valueType
        val seen = mutableSetOf<String>()
        while (current is AppFunctionReferenceTypeMetadata) {
            check(seen.add(current.referenceDataType)) { "Cyclic health result metadata" }
            current = checkNotNull(components.dataTypes[current.referenceDataType]) {
                "Missing health result metadata"
            }
        }
        check(current is AppFunctionObjectTypeMetadata) { "Health result must be an object" }
        return current
    }

    companion object {
        const val HEALTH_ID = "com.keelim.appfunction.health.HealthAppFunctions#getHealthStatus"
        const val JSON_ID = "com.keelim.appfunction.json.JsonFormatterAppFunctions#formatJson"
    }
}
