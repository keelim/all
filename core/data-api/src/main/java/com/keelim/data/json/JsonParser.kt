package com.keelim.data.json

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

inline fun <reified T> Json.decodeOrNull(jsonString: String): T? = try {
    decodeFromString<T>(jsonString)
} catch (_: Exception) {
    null
}

fun Json.formatJson(jsonString: String): String =
    encodeToString(JsonElement.serializer(), parseToJsonElement(jsonString))
