package com.keelim.appfunction.service

import android.app.appfunctions.AppFunctionException
import android.app.appfunctions.ExecuteAppFunctionRequest as PlatformExecuteAppFunctionRequest
import android.app.appfunctions.ExecuteAppFunctionResponse as PlatformExecuteAppFunctionResponse
import android.app.appsearch.GenericDocument as PlatformGenericDocument
import android.app.appsearch.util.DocumentIdUtil
import android.content.pm.SigningInfo
import android.os.CancellationSignal
import android.os.Looper
import android.os.OutcomeReceiver
import androidx.appsearch.app.AppSearchBatchResult
import androidx.appsearch.app.GenericDocument
import androidx.appsearch.app.GlobalSearchSession
import androidx.appsearch.app.SearchResults
import androidx.appsearch.app.SearchSpec
import androidx.appsearch.app.GetByDocumentIdRequest
import androidx.appsearch.platformstorage.PlatformStorage
import com.google.common.util.concurrent.Futures
import com.keelim.data.json.formatJson
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
@LooperMode(LooperMode.Mode.PAUSED)
class KeelimLegacyIndexerIntegrationTest {
    private lateinit var searchSession: GlobalSearchSession
    private lateinit var emptySearchResults: SearchResults
    private val schemaNameOverrides = mutableMapOf<String, String>()

    @Before
    fun setUp() {
        schemaNameOverrides.clear()
        searchSession = mockk(relaxed = true)
        emptySearchResults = mockk(relaxed = true)

        mockkStatic(PlatformStorage::class)
        every {
            PlatformStorage.createGlobalSearchSessionAsync(any())
        } returns Futures.immediateFuture(searchSession)

        every {
            searchSession.getByDocumentIdAsync(any(), any(), any())
        } answers {
            val request = thirdArg<GetByDocumentIdRequest>()
            val documentId = request.ids.single()
            val packageName = documentId.substringBefore('/')
            val functionId = documentId.substringAfter('/')
            val document = when (request.namespace) {
                STATIC_NAMESPACE -> {
                    staticDocument(packageName, functionId)
                }
                RUNTIME_NAMESPACE -> runtimeDocument(packageName, functionId)
                else -> null
            }
            val result = AppSearchBatchResult.Builder<String, GenericDocument>().apply {
                document?.let { setSuccess(documentId, it) }
            }.build()
            Futures.immediateFuture(result)
        }
        every {
            searchSession.search(any<String>(), any<SearchSpec>())
        } returns emptySearchResults
        every {
            emptySearchResults.nextPageAsync
        } returns Futures.immediateFuture(emptyList())
    }

    @After
    fun tearDown() {
        unmockkStatic(PlatformStorage::class)
    }

    @Test
    fun legacyHealthFunctionUsesGeneratedSchemaInventoryAndRealAppSearchReader() {
        val outcome = execute(
            functionId = HEALTH_FUNCTION_ID,
            inputJson = null,
        )

        assertEquals(1, outcome.callbackCount.get())
        assertNull(outcome.error.get())
        val resultDocument = assertNotNullAndGet(outcome.response.get())
            .resultDocument
            .getPropertyDocument(PlatformExecuteAppFunctionResponse.PROPERTY_RETURN_VALUE)
        assertNotNull(resultDocument)
        assertEquals("ok", resultDocument!!.getPropertyString("status"))
        assertEquals("feature:app-function", resultDocument.getPropertyString("module"))
        assertTrue(resultDocument.getPropertyLong("epochMillis") > 0L)
    }

    @Test
    fun legacyJsonFunctionUsesGeneratedSchemaInventoryAndRealAppSearchReader() {
        val inputJson = """{"name":"keelim"}"""
        val outcome = execute(
            functionId = JSON_FUNCTION_ID,
            inputJson = inputJson,
        )

        assertEquals(1, outcome.callbackCount.get())
        assertNull(outcome.error.get())
        assertEquals(
            Json { prettyPrint = true }.formatJson(inputJson),
            assertNotNullAndGet(outcome.response.get())
                .resultDocument
                .getPropertyString(PlatformExecuteAppFunctionResponse.PROPERTY_RETURN_VALUE),
        )
    }

    @Test
    fun unmatchedLegacySchemaFailsAsFunctionNotFound() {
        schemaNameOverrides[HEALTH_FUNCTION_ID] = "unregisteredHealthSchema"

        val outcome = execute(
            functionId = HEALTH_FUNCTION_ID,
            inputJson = null,
        )

        assertEquals(1, outcome.callbackCount.get())
        assertNull(outcome.response.get())
        assertEquals(
            AppFunctionException.ERROR_FUNCTION_NOT_FOUND,
            assertNotNullAndGet(outcome.error.get()).errorCode,
        )
    }

    private fun execute(
        functionId: String,
        inputJson: String?,
    ): CallbackOutcome {
        val serviceController = Robolectric.buildService(KeelimPlatformAppFunctionService::class.java)
            .create()
        try {
            val service = serviceController.get()
            val callbackLatch = CountDownLatch(1)
            val callbackCount = AtomicInteger()
            val response = AtomicReference<PlatformExecuteAppFunctionResponse?>()
            val error = AtomicReference<AppFunctionException?>()
            val callback = object : OutcomeReceiver<PlatformExecuteAppFunctionResponse, AppFunctionException> {
                override fun onResult(result: PlatformExecuteAppFunctionResponse) {
                    callbackCount.incrementAndGet()
                    response.set(result)
                    callbackLatch.countDown()
                }

                override fun onError(result: AppFunctionException) {
                    callbackCount.incrementAndGet()
                    error.set(result)
                    callbackLatch.countDown()
                }
            }

            service.onExecuteFunction(
                platformRequest(service.packageName, functionId, inputJson),
                SYSTEM_PACKAGE_NAME,
                SigningInfo(),
                CancellationSignal(),
                callback,
            )
            awaitCallback(callbackLatch)
            return CallbackOutcome(callbackCount, response, error)
        } finally {
            serviceController.destroy()
        }
    }

    private fun awaitCallback(callbackLatch: CountDownLatch) {
        val mainLooper = shadowOf(Looper.getMainLooper())
        val deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(CALLBACK_TIMEOUT_SECONDS)
        while (callbackLatch.count > 0L && System.nanoTime() < deadline) {
            mainLooper.idle()
            if (callbackLatch.count > 0L) {
                callbackLatch.await(CALLBACK_POLL_MILLIS, TimeUnit.MILLISECONDS)
            }
        }
        mainLooper.idle()
        assertTrue("App Function callback timed out", callbackLatch.count == 0L)
    }

    private fun platformRequest(
        packageName: String,
        functionId: String,
        inputJson: String?,
    ): PlatformExecuteAppFunctionRequest {
        val parameters = PlatformGenericDocument.Builder("", "", "")
            .apply {
                inputJson?.let { setPropertyString("inputJson", it) }
            }
            .build()
        return PlatformExecuteAppFunctionRequest.Builder(packageName, functionId)
            .setParameters(parameters)
            .build()
    }

    private fun staticDocument(
        packageName: String,
        functionId: String,
    ) = GenericDocument.Builder(
        STATIC_NAMESPACE,
        documentId(packageName, functionId),
        "AppFunctionStaticMetadata-$packageName",
    ).setParentTypes(listOf("AppFunctionStaticMetadata"))
        .setPropertyBoolean("enabledByDefault", true)
        .setPropertyString("schemaCategory", schemaCategory(functionId))
        .setPropertyString(
            "schemaName",
            schemaNameOverrides[functionId] ?: schemaName(functionId),
        )
        .setPropertyLong("schemaVersion", 1L)
        .build()

    private fun runtimeDocument(
        packageName: String,
        functionId: String,
    ): GenericDocument {
        val staticId = documentId(packageName, functionId)
        return GenericDocument.Builder(
            RUNTIME_NAMESPACE,
            staticId,
            "AppFunctionRuntimeMetadata",
        ).setPropertyString("functionId", functionId)
            .setPropertyString("packageName", packageName)
            .setPropertyLong("enabled", ENABLED_STATE_DEFAULT)
            .setPropertyString(
                "appFunctionStaticMetadataQualifiedId",
                DocumentIdUtil.createQualifiedId(
                    SYSTEM_PACKAGE_NAME,
                    STATIC_DATABASE,
                    STATIC_NAMESPACE,
                    staticId,
                ),
            )
            .build()
    }

    private fun documentId(packageName: String, functionId: String) = "$packageName/$functionId"

    private fun schemaCategory(functionId: String) = when (functionId) {
        HEALTH_FUNCTION_ID -> "com.keelim.appfunction.health"
        JSON_FUNCTION_ID -> "com.keelim.appfunction.json"
        else -> error("No schema category for $functionId")
    }

    private fun schemaName(functionId: String) = when (functionId) {
        HEALTH_FUNCTION_ID -> "getHealthStatus"
        JSON_FUNCTION_ID -> "formatJson"
        else -> error("No schema name for $functionId")
    }

    private fun <T : Any> assertNotNullAndGet(value: T?): T {
        assertNotNull(value)
        return value!!
    }

    private data class CallbackOutcome(
        val callbackCount: AtomicInteger,
        val response: AtomicReference<PlatformExecuteAppFunctionResponse?>,
        val error: AtomicReference<AppFunctionException?>,
    )

    private companion object {
        const val SYSTEM_PACKAGE_NAME = "android"
        const val STATIC_NAMESPACE = "app_functions"
        const val RUNTIME_NAMESPACE = "app_functions_runtime"
        const val STATIC_DATABASE = "apps-db"
        const val ENABLED_STATE_DEFAULT = 0L
        const val CALLBACK_TIMEOUT_SECONDS = 5L
        const val CALLBACK_POLL_MILLIS = 10L

        const val HEALTH_FUNCTION_ID =
            "com.keelim.appfunction.health.HealthAppFunctions#getHealthStatus"
        const val JSON_FUNCTION_ID =
            "com.keelim.appfunction.json.JsonFormatterAppFunctions#formatJson"
    }
}
