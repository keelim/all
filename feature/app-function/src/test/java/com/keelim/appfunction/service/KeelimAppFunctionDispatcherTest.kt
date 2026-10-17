package com.keelim.appfunction.service

import android.os.CancellationSignal
import androidx.appfunctions.AppFunctionAppUnknownException
import androidx.appfunctions.AppFunctionCancelledException
import androidx.appfunctions.AppFunctionData
import androidx.appfunctions.AppFunctionFunctionNotFoundException
import androidx.appfunctions.AppFunctionInvalidArgumentException
import androidx.appfunctions.ExecuteAppFunctionRequest
import androidx.appfunctions.ExecuteAppFunctionResponse
import androidx.appfunctions.metadata.AppFunctionComponentsMetadata
import androidx.appfunctions.metadata.AppFunctionLongTypeMetadata
import androidx.appfunctions.metadata.AppFunctionMetadata
import androidx.appfunctions.metadata.AppFunctionName
import androidx.appfunctions.metadata.AppFunctionObjectTypeMetadata
import androidx.appfunctions.metadata.AppFunctionPackageMetadata
import androidx.appfunctions.metadata.AppFunctionParameterMetadata
import androidx.appfunctions.metadata.AppFunctionResponseMetadata
import androidx.appfunctions.metadata.AppFunctionStringTypeMetadata
import com.keelim.appfunction.health.HealthStatus
import com.keelim.data.json.formatJson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import java.util.concurrent.Executor
import java.util.function.Consumer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
@LooperMode(LooperMode.Mode.PAUSED)
@OptIn(ExperimentalCoroutinesApi::class)
class KeelimAppFunctionDispatcherTest {
    @Test
    fun healthFunctionIdReturnsWireEncodedHealthStatus() = runTest {
        val metadata = healthMetadata()
        val dispatcher = dispatcher(testScheduler)
        val callbacks = mutableListOf<ExecuteAppFunctionResponse>()

        dispatcher.execute(
            request(metadata),
            metadata,
            CancellationSignal(),
            Consumer(callbacks::add),
        )
        runCurrent()

        assertEquals(1, callbacks.size)
        val success = callbacks.single() as ExecuteAppFunctionResponse.Success
        val result = success.returnValue.getAppFunctionData(
            ExecuteAppFunctionResponse.Success.PROPERTY_RETURN_VALUE,
        )!!
        assertEquals("ok", result.getString("status"))
        assertEquals("feature:app-function", result.getString("module"))
        assertTrue(result.getLong("epochMillis") > 0L)
        dispatcher.close()
    }

    @Test
    fun jsonFunctionIdReturnsFormattedJson() = runTest {
        val metadata = jsonMetadata()
        val dispatcher = dispatcher(testScheduler)
        val callbacks = mutableListOf<ExecuteAppFunctionResponse>()

        dispatcher.execute(
            request(metadata, """{"name":"keelim"}"""),
            metadata,
            CancellationSignal(),
            Consumer(callbacks::add),
        )
        runCurrent()

        assertEquals(1, callbacks.size)
        val success = callbacks.single() as ExecuteAppFunctionResponse.Success
        assertEquals(
            kotlinx.serialization.json.Json { prettyPrint = true }
                .formatJson("""{"name":"keelim"}"""),
            success.returnValue.getString(ExecuteAppFunctionResponse.Success.PROPERTY_RETURN_VALUE),
        )
        dispatcher.close()
    }

    @Test
    fun invalidJsonRequestsReturnTypedInvalidArgumentErrorsAndStableMessages() = runTest {
        val metadata = jsonMetadata()
        val dispatcher = dispatcher(testScheduler)
        val invalidInputs = listOf(
            " " to "JSON input must not be blank",
            "x".repeat(65_537) to "JSON input must not exceed 65536 characters",
            "{" to "JSON input must be valid JSON",
        )

        invalidInputs.forEach { (input, expectedMessage) ->
            val callbacks = mutableListOf<ExecuteAppFunctionResponse>()
            dispatcher.execute(
                request(metadata, input),
                metadata,
                CancellationSignal(),
                Consumer(callbacks::add),
            )
            runCurrent()

            assertEquals(1, callbacks.size)
            val error = (callbacks.single() as ExecuteAppFunctionResponse.Error).error
            assertTrue(error is AppFunctionInvalidArgumentException)
            assertEquals(expectedMessage, error.errorMessage)
        }

        dispatcher.close()
    }

    @Test
    fun missingJsonParameterReturnsTypedInvalidArgumentError() = runTest {
        val metadata = jsonMetadata()
        val dispatcher = dispatcher(testScheduler)
        val callbacks = mutableListOf<ExecuteAppFunctionResponse>()
        val optionalInputJson = AppFunctionParameterMetadata(
            name = "inputJson",
            isRequired = false,
            dataType = AppFunctionStringTypeMetadata(isNullable = false),
        )
        val emptyParameters = AppFunctionData.Builder(
            listOf(optionalInputJson),
            metadata.packageMetadata.components,
        ).build()
        val request = ExecuteAppFunctionRequest(PACKAGE_NAME, metadata.id, emptyParameters)

        dispatcher.execute(request, metadata, CancellationSignal(), Consumer(callbacks::add))
        runCurrent()

        assertEquals(1, callbacks.size)
        val error = (callbacks.single() as ExecuteAppFunctionResponse.Error).error
        assertTrue(error is AppFunctionInvalidArgumentException)
        assertEquals("Missing inputJson parameter", error.errorMessage)
        dispatcher.close()
    }

    @Test
    fun unknownFunctionIdReturnsAnErrorExactlyOnce() = runTest {
        val metadata = jsonMetadata()
        val dispatcher = dispatcher(testScheduler)
        val callbacks = mutableListOf<ExecuteAppFunctionResponse>()
        val unknownRequest = ExecuteAppFunctionRequest(
            PACKAGE_NAME,
            "com.keelim.appfunction.missing.UnknownFunctions#missing",
            AppFunctionData.Builder(metadata.parameters, metadata.packageMetadata.components)
                .setString("inputJson", "{}")
                .build(),
        )

        dispatcher.execute(unknownRequest, metadata, CancellationSignal(), Consumer(callbacks::add))
        runCurrent()

        assertEquals(1, callbacks.size)
        val error = (callbacks.single() as ExecuteAppFunctionResponse.Error).error
        assertTrue(error is AppFunctionFunctionNotFoundException)
        assertEquals("${unknownRequest.functionIdentifier} is not available", error.errorMessage)
        dispatcher.close()
    }

    @Test
    fun precancelledRequestReturnsCancellationExactlyOnce() = runTest {
        val metadata = jsonMetadata()
        val dispatcher = dispatcher(testScheduler)
        val callbacks = mutableListOf<ExecuteAppFunctionResponse>()
        val cancellationSignal = CancellationSignal().apply { cancel() }

        dispatcher.execute(
            request(metadata, "{}"),
            metadata,
            cancellationSignal,
            Consumer(callbacks::add),
        )

        assertEquals(1, callbacks.size)
        val error = (callbacks.single() as ExecuteAppFunctionResponse.Error).error
        assertTrue(error is AppFunctionCancelledException)
        dispatcher.close()
    }

    @Test
    fun cancellationAfterExecuteButBeforeCoroutineStartsReturnsCancellationOnce() = runTest {
        val metadata = jsonMetadata()
        val dispatcher = dispatcher(testScheduler)
        val callbacks = mutableListOf<ExecuteAppFunctionResponse>()
        val cancellationSignal = CancellationSignal()

        dispatcher.execute(
            request(metadata, "{}"),
            metadata,
            cancellationSignal,
            Consumer(callbacks::add),
        )
        cancellationSignal.cancel()
        runCurrent()

        assertEquals(1, callbacks.size)
        val error = (callbacks.single() as ExecuteAppFunctionResponse.Error).error
        assertTrue(error is AppFunctionCancelledException)
        dispatcher.close()
    }

    @Test
    fun cancellationDuringExecutionReturnsCancellationExactlyOnce() = runTest {
        val metadata = jsonMetadata()
        val dispatcher = dispatcher(testScheduler) { _, _ -> awaitCancellation() }
        val callbacks = mutableListOf<ExecuteAppFunctionResponse>()
        val cancellationSignal = CancellationSignal()

        dispatcher.execute(
            request(metadata, "{}"),
            metadata,
            cancellationSignal,
            Consumer(callbacks::add),
        )
        runCurrent()
        cancellationSignal.cancel()
        runCurrent()

        assertEquals(1, callbacks.size)
        val error = (callbacks.single() as ExecuteAppFunctionResponse.Error).error
        assertTrue(error is AppFunctionCancelledException)
        dispatcher.close()
    }

    @Test
    fun closingDuringExecutionReturnsCancellationExactlyOnce() = runTest {
        val metadata = jsonMetadata()
        val dispatcher = dispatcher(testScheduler) { _, _ -> awaitCancellation() }
        val callbacks = mutableListOf<ExecuteAppFunctionResponse>()

        dispatcher.execute(
            request(metadata, "{}"),
            metadata,
            CancellationSignal(),
            Consumer(callbacks::add),
        )
        runCurrent()
        dispatcher.close()
        runCurrent()

        assertEquals(1, callbacks.size)
        val error = (callbacks.single() as ExecuteAppFunctionResponse.Error).error
        assertTrue(error is AppFunctionCancelledException)
    }

    @Test
    fun cancellationDuringNonSuspendingOperationOverridesSuccessOnce() = runTest {
        val metadata = jsonMetadata()
        val cancellationSignal = CancellationSignal()
        val dispatcher = dispatcher(testScheduler) { _, functionMetadata ->
            cancellationSignal.cancel()
            ExecuteAppFunctionResponse.Success(
                AppFunctionData.Builder(
                    functionMetadata.response,
                    functionMetadata.packageMetadata.components,
                ).setString(ExecuteAppFunctionResponse.Success.PROPERTY_RETURN_VALUE, "{}")
                    .build(),
            )
        }
        val callbacks = mutableListOf<ExecuteAppFunctionResponse>()

        dispatcher.execute(
            request(metadata, "{}"),
            metadata,
            cancellationSignal,
            Consumer(callbacks::add),
        )
        runCurrent()

        assertEquals(1, callbacks.size)
        val error = (callbacks.single() as ExecuteAppFunctionResponse.Error).error
        assertTrue(error is AppFunctionCancelledException)
        dispatcher.close()
    }

    @Test
    fun unexpectedExceptionReturnsAppUnknownErrorExactlyOnce() = runTest {
        val metadata = jsonMetadata()
        val dispatcher = dispatcher(testScheduler) { _, _ -> error("unexpected failure") }
        val callbacks = mutableListOf<ExecuteAppFunctionResponse>()

        dispatcher.execute(
            request(metadata, "{}"),
            metadata,
            CancellationSignal(),
            Consumer(callbacks::add),
        )
        runCurrent()

        assertEquals(1, callbacks.size)
        val error = (callbacks.single() as ExecuteAppFunctionResponse.Error).error
        assertTrue(error is AppFunctionAppUnknownException)
        assertEquals("unexpected failure", error.errorMessage)
        dispatcher.close()
    }

    private fun dispatcher(
        scheduler: TestCoroutineScheduler,
        operation: suspend (ExecuteAppFunctionRequest, AppFunctionMetadata) -> ExecuteAppFunctionResponse =
            KeelimAppFunctionRequestHandler()::execute,
    ) = KeelimAppFunctionDispatcher(
        workDispatcher = StandardTestDispatcher(scheduler),
        callbackExecutor = Executor { it.run() },
        operation = operation,
    )

    private fun request(
        metadata: AppFunctionMetadata,
        inputJson: String? = null,
    ): ExecuteAppFunctionRequest {
        val parameters = AppFunctionData.Builder(
            metadata.parameters,
            metadata.packageMetadata.components,
        ).apply {
            inputJson?.let { setString("inputJson", it) }
        }.build()
        return ExecuteAppFunctionRequest(PACKAGE_NAME, metadata.id, parameters)
    }

    private fun healthMetadata(): AppFunctionMetadata {
        val components = AppFunctionComponentsMetadata()
        val healthType = AppFunctionObjectTypeMetadata(
            properties = mapOf(
                "status" to AppFunctionStringTypeMetadata(isNullable = false),
                "module" to AppFunctionStringTypeMetadata(isNullable = false),
                "epochMillis" to AppFunctionLongTypeMetadata(isNullable = false),
            ),
            required = listOf("status", "module", "epochMillis"),
            qualifiedName = HealthStatus::class.java.name,
            isNullable = false,
        )
        return metadata(
            id = KeelimAppFunctionRequestHandler.HEALTH_ID,
            parameters = emptyList(),
            responseType = healthType,
            components = components,
        )
    }

    private fun jsonMetadata(): AppFunctionMetadata {
        val components = AppFunctionComponentsMetadata()
        return metadata(
            id = KeelimAppFunctionRequestHandler.JSON_ID,
            parameters = listOf(
                AppFunctionParameterMetadata(
                    name = "inputJson",
                    isRequired = true,
                    dataType = AppFunctionStringTypeMetadata(isNullable = false),
                ),
            ),
            responseType = AppFunctionStringTypeMetadata(isNullable = false),
            components = components,
        )
    }

    private fun metadata(
        id: String,
        parameters: List<AppFunctionParameterMetadata>,
        responseType: androidx.appfunctions.metadata.AppFunctionDataTypeMetadata,
        components: AppFunctionComponentsMetadata,
    ): AppFunctionMetadata {
        val packageMetadata = AppFunctionPackageMetadata(PACKAGE_NAME, components)
        return AppFunctionMetadata(
            name = AppFunctionName(PACKAGE_NAME, id),
            schema = null,
            parameters = parameters,
            response = AppFunctionResponseMetadata(responseType),
            packageMetadata = packageMetadata,
        )
    }

    private companion object {
        const val PACKAGE_NAME = "com.keelim.appfunction"
    }
}
