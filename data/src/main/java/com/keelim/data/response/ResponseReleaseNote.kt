package com.keelim.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class CreatedBy(
    @SerialName("id")
    val id: String, // 761cccfd-0a33-4774-8689-fd544502b79e
    @SerialName("object")
    val objectX: String, // user
)

@Serializable
data class LastEditedBy(
    @SerialName("id")
    val id: String, // 761cccfd-0a33-4774-8689-fd544502b79e
    @SerialName("object")
    val objectX: String, // user
)

@Serializable
data class Parent(
    @SerialName("type")
    val type: String, // workspace
    @SerialName("workspace")
    val workspace: Boolean, // true
)

@Serializable
data class Properties(
    @SerialName("com.keelim.data.response.이름")
    val 이름: 이름,
    @SerialName("com.keelim.data.response.태그")
    val 태그: 태그,
)

@Serializable
data class TitleX(
    @SerialName("annotations")
    val annotations: Annotations,
    @SerialName("href")
    val href: String, // null
    @SerialName("plain_text")
    val plainText: String, // MyGrade
    @SerialName("text")
    val text: Text,
    @SerialName("type")
    val type: String, // text
)
@Serializable
class Title

@Serializable
data class MultiSelect(
    @SerialName("options")
    val options: List<Option>,
)

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
