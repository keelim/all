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
     */
    @AppFunction(isDescribedByKdoc = true)
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
