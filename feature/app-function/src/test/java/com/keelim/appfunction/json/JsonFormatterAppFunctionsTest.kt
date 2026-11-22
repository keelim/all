package com.keelim.appfunction.json

import android.os.Build
import com.keelim.core.data.json.DefaultJsonParser
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ServiceController
import org.robolectric.annotation.Config

/** Runs the generated stable-API service at AppFunctions' minimum supported Android API. */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36], manifest = Config.NONE)
class JsonFormatterAppFunctionsTest {
    private lateinit var controller: ServiceController<JsonFormatterAppFunctionService>
    private val jsonParser = DefaultJsonParser(Json { prettyPrint = true })

    @Before
    fun createService() {
        Build.VERSION.SDK_INT shouldBe 36
        Runtime.version().feature() shouldBe 21
        controller = Robolectric.buildService(JsonFormatterAppFunctionService::class.java).create()
    }

    @After
    fun destroyService() {
        controller.destroy()
    }

    @Test
    fun preservesExternalFunctionIdentifier() {
        JsonFormatterAppFunctionService.FUNCTION_ID_FORMAT_JSON shouldBe
            "com.keelim.appfunction.json.JsonFormatterAppFunctions#formatJson"
    }

    @Test
    fun formatsJsonThroughTheSameParserUsedByTheApp() = runTest {
        val input = """{"name":"keelim"}"""
        controller.get().formatJson(input) shouldBe jsonParser.formatJson(input)
    }

    @Test
    fun rejectsBlankOversizedAndInvalidInputWithStableMessages() = runTest {
        val appFunctions = controller.get()
        shouldThrow<IllegalArgumentException> {
            appFunctions.formatJson(" ")
        }.message shouldBe "JSON input must not be blank"
        shouldThrow<IllegalArgumentException> {
            appFunctions.formatJson("x".repeat(65_537))
        }.message shouldBe "JSON input must not exceed 65536 characters"
        shouldThrow<IllegalArgumentException> {
            appFunctions.formatJson("{")
        }.message shouldBe "JSON input must be valid JSON"
    }
}
