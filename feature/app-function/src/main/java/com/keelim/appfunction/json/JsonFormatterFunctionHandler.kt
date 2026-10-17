package com.keelim.appfunction.json

import com.keelim.data.json.formatJson
import kotlinx.serialization.json.Json

private const val MAX_JSON_INPUT_LENGTH = 64 * 1024

internal class JsonFormatterFunctionHandler(
    private val json: Json = Json { prettyPrint = true },
) {
    suspend fun formatJson(inputJson: String): String {
        require(inputJson.isNotBlank()) { "JSON input must not be blank" }
        require(inputJson.length <= MAX_JSON_INPUT_LENGTH) {
            "JSON input must not exceed $MAX_JSON_INPUT_LENGTH characters"
        }

        return try {
            json.formatJson(inputJson)
        } catch (error: IllegalArgumentException) {
            throw IllegalArgumentException("JSON input must be valid JSON", error)
        }
    }
}
