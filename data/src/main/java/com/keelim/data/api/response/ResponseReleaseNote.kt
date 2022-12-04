import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseNote(
    @SerialName("archived")
    val archived: Boolean, // false
    @SerialName("cover")
    val cover: String, // null
    @SerialName("created_by")
    val createdBy: CreatedBy,
    @SerialName("created_time")
    val createdTime: String, // 2022-07-24T10:27:00.000Z
    @SerialName("description")
    val description: List<String>,
    @SerialName("icon")
    val icon: String, // null
    @SerialName("id")
    val id: String, // 17100f6b-a057-402a-b5ec-0cf0123c0207
    @SerialName("is_inline")
    val isInline: Boolean, // false
    @SerialName("last_edited_by")
    val lastEditedBy: LastEditedBy,
    @SerialName("last_edited_time")
    val lastEditedTime: String, // 2022-07-24T10:36:00.000Z
    @SerialName("object")
    val objectX: String, // database
    @SerialName("parent")
    val parent: Parent,
    @SerialName("properties")
    val properties: Properties,
    @SerialName("title")
    val title: List<TitleX>,
    @SerialName("url")
    val url: String, // https://www.notion.so/17100f6ba057402ab5ec0cf0123c0207
)

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
    @SerialName("이름")
    val 이름: 이름,
    @SerialName("태그")
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
data class 이름(
    @SerialName("id")
    val id: String, // title
    @SerialName("name")
    val name: String, // 이름
    @SerialName("title")
    val title: Title,
    @SerialName("type")
    val type: String, // title
)

@Serializable
data class 태그(
    @SerialName("id")
    val id: String, // GO%7Cg
    @SerialName("multi_select")
    val multiSelect: MultiSelect,
    @SerialName("name")
    val name: String, // 태그
    @SerialName("type")
    val type: String, // multi_select
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
