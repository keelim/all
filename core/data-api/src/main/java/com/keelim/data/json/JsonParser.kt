package com.keelim.data.json

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.JsonElement

/**
 * JSON 파싱을 위한 인터페이스
 */
interface JsonParser {
    /**
     * JSON 문자열을 주어진 타입으로 디코딩합니다.
     * @param jsonString JSON 문자열
     * @param deserializer 역직렬화기
     * @return 디코딩된 객체
     */
    fun <T> decodeFromString(jsonString: String, deserializer: DeserializationStrategy<T>): T

    /**
     * JSON 문자열을 주어진 타입으로 디코딩합니다. 실패 시 null을 반환합니다.
     * @param jsonString JSON 문자열
     * @param deserializer 역직렬화기
     * @return 디코딩된 객체 또는 실패 시 null
     */
    fun <T> decodeFromStringOrNull(jsonString: String, deserializer: DeserializationStrategy<T>): T?

    /**
     * 객체를 JSON 문자열로 인코딩합니다.
     * @param serializer 직렬화기
     * @param value 인코딩할 객체
     * @return JSON 문자열
     */
    fun <T> encodeToString(serializer: SerializationStrategy<T>, value: T): String

    /**
     * JSON 문자열을 파싱하여 JsonElement로 변환합니다.
     * @param jsonString JSON 문자열
     * @return JsonElement
     */
    fun parseToJsonElement(jsonString: String): JsonElement

    /**
     * JSON 문자열을 포맷팅합니다 (pretty print).
     * @param jsonString JSON 문자열
     * @return 포맷팅된 JSON 문자열
     */
    fun formatJson(jsonString: String): String
}

