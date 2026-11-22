package com.keelim.appfunction.json

import androidx.annotation.RequiresApi
import androidx.appfunctions.AppFunctionDeclaration
import androidx.appfunctions.AppFunctionService
import androidx.appfunctions.AppFunctionServiceEntryPoint
import com.keelim.core.data.json.DefaultJsonParser
import com.keelim.data.json.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

private const val MAX_JSON_INPUT_LENGTH = 64 * 1024

/** Read-only JSON functions; the class name preserves the existing external function identifier. */
@RequiresApi(36)
@AppFunctionServiceEntryPoint(
    serviceName = "JsonFormatterAppFunctionService",
    appFunctionXmlFileName = "json_formatter_app_functions",
)
abstract class JsonFormatterAppFunctions internal constructor(
    private val jsonParser: JsonParser,
) : AppFunctionService() {
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
     * @param inputJson The raw JSON string to pretty-print. Must be valid JSON up to 65,536 characters.
     * @return The formatted pretty-printed JSON string.
     * @throws IllegalArgumentException If [inputJson] is blank, exceeds 65,536 characters, or is not valid JSON.
     */
    @AppFunctionDeclaration(isDescribedByKDoc = true)
    suspend fun formatJson(
        inputJson: String,
    ): String = withContext(Dispatchers.Default) {
        require(inputJson.isNotBlank()) { "JSON input must not be blank" }
        require(inputJson.length <= MAX_JSON_INPUT_LENGTH) {
            "JSON input must not exceed $MAX_JSON_INPUT_LENGTH characters"
        }

        try {
            jsonParser.formatJson(inputJson)
        } catch (error: IllegalArgumentException) {
            throw IllegalArgumentException("JSON input must be valid JSON", error)
        }
    }
}
