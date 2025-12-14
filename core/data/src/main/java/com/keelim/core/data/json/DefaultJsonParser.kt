package com.keelim.core.data.json

import com.keelim.data.json.JsonParser
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import javax.inject.Inject

class DefaultJsonParser @Inject constructor(
    private val json: Json,
) : JsonParser {

    override fun <T> decodeFromString(
        jsonString: String,
        deserializer: DeserializationStrategy<T>,
    ): T = json.decodeFromString(deserializer, jsonString)

    override fun <T> decodeFromStringOrNull(
        jsonString: String,
        deserializer: DeserializationStrategy<T>,
    ): T? = try {
        json.decodeFromString(deserializer, jsonString)
    } catch (e: Exception) {
        null
    }

    override fun <T> encodeToString(
        serializer: SerializationStrategy<T>,
        value: T,
    ): String = json.encodeToString(serializer, value)

    override fun parseToJsonElement(jsonString: String): JsonElement =
        json.parseToJsonElement(jsonString)

    override fun formatJson(jsonString: String): String {
        val element = json.parseToJsonElement(jsonString)
        return json.encodeToString(JsonElement.serializer(), element)
    }
}

