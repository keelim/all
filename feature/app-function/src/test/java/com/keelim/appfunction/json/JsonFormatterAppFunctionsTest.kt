package com.keelim.appfunction.json

import com.keelim.data.json.formatJson
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json

class JsonFormatterAppFunctionsTest : FunSpec({
    val json = Json { prettyPrint = true }
    val handler = JsonFormatterFunctionHandler(json)

    test("formats JSON through the same parser used by the app") {
        runTest {
            val input = """{"name":"keelim"}"""

            handler.formatJson(input) shouldBe
                json.formatJson(input)
        }
    }

    test("rejects blank, oversized, and invalid input with stable messages") {
        runTest {
            shouldThrow<IllegalArgumentException> {
                handler.formatJson(" ")
            }.message shouldBe "JSON input must not be blank"

            shouldThrow<IllegalArgumentException> {
                handler.formatJson("x".repeat(65_537))
            }.message shouldBe "JSON input must not exceed 65536 characters"

            shouldThrow<IllegalArgumentException> {
                handler.formatJson("{")
            }.message shouldBe "JSON input must be valid JSON"
        }
    }
})
