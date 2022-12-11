package com.keelim.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Option(
    @SerialName("color")
    val color: String, // blue
    @SerialName("id")
    val id: String, // 74b15b36-523b-4e2f-af5a-5bafd5123ff8
    @SerialName("name")
    val name: String, // heelo
)

@Serializable
data class Annotations(
    @SerialName("bold")
    val bold: Boolean, // false
    @SerialName("code")
    val code: Boolean, // false
    @SerialName("color")
    val color: String, // default
    @SerialName("italic")
    val italic: Boolean, // false
    @SerialName("strikethrough")
    val strikethrough: Boolean, // false
    @SerialName("underline")
    val underline: Boolean, // false
)

@Serializable
data class Text(
    @SerialName("content")
    val content: String, // MyGrade
    @SerialName("link")
    val link: String, // null
)
