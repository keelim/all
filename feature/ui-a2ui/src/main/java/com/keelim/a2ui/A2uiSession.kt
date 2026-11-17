package com.keelim.a2ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

/** A deliberately limited, read-only A2UI 1.0 session. Call [accept] off the main thread. */
class A2uiSession {
    private val mutableSurfaces = MutableStateFlow<Map<String, A2uiDocument>>(emptyMap())
    val surfaces: StateFlow<Map<String, A2uiDocument>> = mutableSurfaces.asStateFlow()

    /** One complete JSON envelope; failed validation leaves all existing surfaces unchanged. */
    @Synchronized
    fun accept(message: String): A2uiResult = try {
        require(message.length <= MAX_MESSAGE_CHARS) { "Message too large" }
        validateNesting(message)
        val envelope = Json.parseToJsonElement(message).objectValue()
        require(envelope.string("version") == "1.0") { "Unsupported protocol version" }
        val operation = requireNotNull(envelope.keys.singleOrNull { it != "version" }) {
            "Expected exactly one message type"
        }
        require(operation in setOf("createSurface", "updateComponents", "deleteSurface")) {
            "Unsupported message type"
        }
        envelope.only("version", operation)
        val payload = envelope.getValue(operation).objectValue()
        val surfaceId = payload.identifier("surfaceId")
        val current = mutableSurfaces.value
        val next = current.toMutableMap()
        when (operation) {
            "createSurface" -> {
                payload.only("surfaceId", "catalogId", "components")
                require(surfaceId !in current) { "Surface already exists" }
                require(current.size < MAX_SURFACES) { "Too many surfaces" }
                require(payload.string("catalogId") == CATALOG_ID) { "Unsupported catalog" }
                val nodes = payload["components"]?.let(::parseComponents) ?: emptyMap()
                validateGraph(nodes)
                next[surfaceId] = A2uiDocument(nodes)
            }
            "updateComponents" -> {
                payload.only("surfaceId", "components")
                val previous = requireNotNull(current[surfaceId]) { "Unknown surface" }
                val nodes = previous.nodes + parseComponents(payload.getValue("components"))
                validateGraph(nodes)
                next[surfaceId] = A2uiDocument(nodes)
            }
            "deleteSurface" -> {
                payload.only("surfaceId")
                require(surfaceId in current) { "Unknown surface" }
                next.remove(surfaceId)
            }
        }
        mutableSurfaces.value = next.toMap()
        A2uiResult.Accepted
    } catch (_: IllegalArgumentException) {
        // Never echo untrusted payloads (which may contain private data) in diagnostics.
        A2uiResult.Rejected
    } catch (_: NoSuchElementException) {
        A2uiResult.Rejected
    }

    companion object {
        const val CATALOG_ID = "https://github.com/keelim/all/a2ui/read-only/v1"
        const val MAX_MESSAGE_CHARS = 65_536
        const val MAX_COMPONENTS = 128
        const val MAX_SURFACES = 4
        const val MAX_DEPTH = 16
        const val MAX_RENDERED_NODES = 256
    }
}

sealed interface A2uiResult {
    data object Accepted : A2uiResult
    data object Rejected : A2uiResult
}

/** Only the validated session can construct documents. Missing children render placeholders. */
class A2uiDocument internal constructor(internal val nodes: Map<String, A2uiNode>)

internal sealed interface A2uiNode {
    data class Text(val text: String) : A2uiNode
    data class Column(val children: List<String>) : A2uiNode
    data class Row(val children: List<String>) : A2uiNode
    data class Card(val child: String) : A2uiNode
}

private fun parseComponents(value: JsonElement): Map<String, A2uiNode> {
    val components = value as? JsonArray ?: errorInput()
    require(components.size in 1..A2uiSession.MAX_COMPONENTS) { "Invalid component count" }
    val result = mutableMapOf<String, A2uiNode>()
    components.forEach { element ->
        val component = element.objectValue()
        val id = component.identifier("id")
        require(id !in result) { "Duplicate component ID" }
        result[id] = when (component.string("component")) {
            "Text" -> {
                component.only("id", "component", "text")
                A2uiNode.Text(component.string("text").also { require(it.length <= 4096) { "Text too long" } })
            }
            "Column", "Row" -> {
                component.only("id", "component", "children")
                val children = component["children"] as? JsonArray ?: errorInput()
                require(children.size <= A2uiSession.MAX_COMPONENTS) { "Too many children" }
                val ids = children.map { it.stringValue().also(::validateIdentifier) }
                if (component.string("component") == "Column") A2uiNode.Column(ids) else A2uiNode.Row(ids)
            }
            "Card" -> {
                component.only("id", "component", "child")
                A2uiNode.Card(component.identifier("child"))
            }
            else -> errorInput()
        }
    }
    return result.toMap()
}

private fun validateGraph(nodes: Map<String, A2uiNode>) {
    require(nodes.size <= A2uiSession.MAX_COMPONENTS) { "Too many components" }
    // Validate every subtree, including unreachable nodes; bound DAG expansion, not just unique IDs.
    nodes.keys.forEach { root ->
        var count = 0
        val ancestors = mutableSetOf<String>()
        fun visit(id: String, depth: Int) {
            require(depth <= A2uiSession.MAX_DEPTH) { "Component graph too deep" }
            require(++count <= A2uiSession.MAX_RENDERED_NODES) { "Expanded graph too large" }
            require(ancestors.add(id)) { "Cyclic component graph" }
            when (val node = nodes[id]) {
                is A2uiNode.Column -> node.children.forEach { visit(it, depth + 1) }
                is A2uiNode.Row -> node.children.forEach { visit(it, depth + 1) }
                is A2uiNode.Card -> visit(node.child, depth + 1)
                else -> Unit
            }
            ancestors.remove(id)
        }
        visit(root, 1)
    }
}

private fun validateNesting(message: String) {
    var depth = 0
    var quoted = false
    var escaped = false
    message.forEach { char ->
        if (quoted) {
            when {
                escaped -> escaped = false
                char == '\\' -> escaped = true
                char == '"' -> quoted = false
            }
        } else {
            when (char) {
                '"' -> quoted = true
                '{', '[' -> require(++depth <= 32) { "JSON too deep" }
                '}', ']' -> depth--
            }
        }
    }
}

private fun JsonElement.objectValue(): JsonObject = this as? JsonObject ?: errorInput()
private fun JsonElement.stringValue(): String {
    val primitive = this as? JsonPrimitive ?: errorInput()
    require(primitive.isString) { "Expected a string" }
    return primitive.content
}
private fun JsonObject.string(key: String): String = getValue(key).stringValue()
private fun JsonObject.identifier(key: String): String = string(key).also(::validateIdentifier)
private fun validateIdentifier(id: String) {
    require(id.matches(Regex("[A-Za-z_][A-Za-z0-9_-]{0,63}"))) { "Invalid identifier" }
}
private fun JsonObject.only(vararg allowed: String) {
    require(keys.all { it in allowed }) { "Unsupported property" }
}
private fun errorInput(): Nothing = throw IllegalArgumentException("Invalid A2UI payload")
