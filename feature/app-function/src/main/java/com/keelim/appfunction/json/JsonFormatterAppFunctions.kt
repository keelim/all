package com.keelim.appfunction.json

import androidx.appfunctions.AppFunctionContext
import androidx.appfunctions.service.AppFunction
import com.keelim.core.data.json.DefaultJsonParser
import com.keelim.data.json.JsonParser
import kotlinx.serialization.json.Json

private const val MAX_JSON_INPUT_LENGTH = 64 * 1024

class JsonFormatterAppFunctions internal constructor(
    private val jsonParser: JsonParser,
) {
    constructor() : this(
        DefaultJsonParser(
            Json {
                prettyPrint = true
            },
        ),
    )

    /**
     * Formats a JSON value without changing application state.
     *
     * Input must be non-blank, valid JSON and at most 65,536 characters.
     *
     * @param appFunctionContext The execution context provided by the AppFunction framework.
     * @param inputJson The raw JSON string to pretty-print. Must be valid JSON up to 65,536 characters.
     * @return The formatted pretty-printed JSON string.
     * @throws IllegalArgumentException If [inputJson] is blank, exceeds 65,536 characters, or is not valid JSON.
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun formatJson(
        appFunctionContext: AppFunctionContext,
        inputJson: String,
    ): String {
        require(inputJson.isNotBlank()) { "JSON input must not be blank" }
        require(inputJson.length <= MAX_JSON_INPUT_LENGTH) {
            "JSON input must not exceed $MAX_JSON_INPUT_LENGTH characters"
        }

        return try {
            jsonParser.formatJson(inputJson)
        } catch (error: IllegalArgumentException) {
            throw IllegalArgumentException("JSON input must be valid JSON", error)
        }
    }
}
