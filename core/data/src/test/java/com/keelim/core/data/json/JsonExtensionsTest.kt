package com.keelim.core.data.json

import com.keelim.data.json.decodeOrNull
import com.keelim.data.json.formatJson
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class JsonExtensionsTest : FunSpec({
    val json = Json {
        prettyPrint = true
        prettyPrintIndent = "  "
    }

    test("Json handles decode, encode, nullable failures, elements, and pretty print") {
        val input = """{"name":"keelim","count":1}"""
        val value = json.decodeFromString<Sample>(input)
        val compactJson = Json()

        value shouldBe Sample(name = "keelim", count = 1)
        compactJson.encodeToString(value) shouldBe input
        json.decodeOrNull<Sample>("{\"name\":}") shouldBe null
        json.parseToJsonElement(input).jsonObject["name"]?.jsonPrimitive?.content shouldBe "keelim"
        json.formatJson(input) shouldBe """{
  "name": "keelim",
  "count": 1
}"""
    }
})

@Serializable
private data class Sample(
    val name: String,
    val count: Int,
)
