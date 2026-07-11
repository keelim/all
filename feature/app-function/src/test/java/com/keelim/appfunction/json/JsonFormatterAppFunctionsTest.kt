package com.keelim.appfunction.json

import android.content.Context
import androidx.appfunctions.AppFunctionContext
import com.keelim.core.data.json.DefaultJsonParser
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json

class JsonFormatterAppFunctionsTest : FunSpec({
    val appFunctionContext = object : AppFunctionContext {
        override val context: Context = mockk(relaxed = true)
    }
    val jsonParser = DefaultJsonParser(Json { prettyPrint = true })
    val appFunctions = JsonFormatterAppFunctions(jsonParser)

    test("formats JSON through the same parser used by the app") {
        runTest {
            val input = """{"name":"keelim"}"""

            appFunctions.formatJson(appFunctionContext, input) shouldBe
                jsonParser.formatJson(input)
        }
    }

    test("rejects blank, oversized, and invalid input with stable messages") {
        runTest {
            shouldThrow<IllegalArgumentException> {
                appFunctions.formatJson(appFunctionContext, " ")
            }.message shouldBe "JSON input must not be blank"

            shouldThrow<IllegalArgumentException> {
                appFunctions.formatJson(appFunctionContext, "x".repeat(65_537))
            }.message shouldBe "JSON input must not exceed 65536 characters"

            shouldThrow<IllegalArgumentException> {
                appFunctions.formatJson(appFunctionContext, "{")
            }.message shouldBe "JSON input must be valid JSON"
        }
    }
})
