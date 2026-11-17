package com.keelim.a2ui

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.put

class A2uiSessionTest : FunSpec({
    test("offline sample creates a native catalog document and escapes untrusted strings") {
        val session = A2uiSession()
        session.accept(sampleEnvelope("title \"quoted\"", "<script>never executable</script>")) shouldBe A2uiResult.Accepted
        session.surfaces.value.getValue("sample").nodes["title"] shouldBe A2uiNode.Text("title \"quoted\"")
    }

    test("updates replace nodes, retain siblings and preserve older snapshots") {
        val session = sampleSession()
        val before = session.surfaces.value.getValue("sample")
        session.accept(update("""{"id":"title","component":"Text","text":"Updated"}""")) shouldBe A2uiResult.Accepted
        val after = session.surfaces.value.getValue("sample")
        after.nodes["title"] shouldBe A2uiNode.Text("Updated")
        after.nodes["body"] shouldBe before.nodes["body"]
        before.nodes["title"] shouldBe A2uiNode.Text("Title")
        session.accept("""{"version":"1.0","deleteSurface":{"surfaceId":"sample"}}""") shouldBe A2uiResult.Accepted
        session.surfaces.value.isEmpty() shouldBe true
    }

    test("incomplete trees can arrive progressively") {
        val session = A2uiSession()
        session.accept(create("""{"id":"root","component":"Column","children":["later"]}""")) shouldBe A2uiResult.Accepted
        session.accept(update("""{"id":"later","component":"Text","text":"Ready"}""")) shouldBe A2uiResult.Accepted
    }

    test("Row supports literal children while empty component arrays are invalid") {
        val session = A2uiSession()
        session.accept(create("""{"id":"root","component":"Row","children":["left","right"]}""")) shouldBe A2uiResult.Accepted
        session.accept(update("""{"id":"left","component":"Text","text":"Left"},{"id":"right","component":"Text","text":"Right"}""")) shouldBe A2uiResult.Accepted
        session.surfaces.value.getValue("sample").nodes["root"] shouldBe A2uiNode.Row(listOf("left", "right"))
        session.accept(update("")) shouldBe A2uiResult.Rejected
    }

    test("reject malformed input and unsupported capabilities atomically") {
        val messages = listOf(
            "not JSON", "[]", "null", "{}",
            """{"version":"0.9","deleteSurface":{"surfaceId":"sample"}}""",
            """{"version":"1.0","updateDataModel":{"surfaceId":"sample","value":{}}}""",
            """{"version":"1.0","callRendererFunction":{"callFunction":{"@call":"openUrl"}}}""",
            """{"version":"1.0","deleteSurface":{"surfaceId":"missing"}}""",
            """{"version":"1.0","deleteSurface":{"surfaceId":123}}""",
            """{"version":"1.0","deleteSurface":{"surfaceId":"sample"},"extra":{}}""",
            create("", catalog = "https://attacker.invalid/catalog"),
            create("""{"id":"root","component":"Text","text":"duplicate surface"}"""),
            update("""{"id":"root","component":"WebView","url":"javascript:alert(1)"}"""),
            update("""{"id":"root","component":"Text","text":{"@path":"/secret"}}"""),
            update("""{"id":"root","component":"Text","text":"a","onClick":"run"}"""),
            update("""{"id":"root","component":"Text","text":null}"""),
            update("""{"id":"root","component":"Text","text":true}"""),
            update("""{"id":"root","component":"Text"}"""),
            update("""{"id":"root","component":"Column","children":{}}"""),
            update("""{"id":"root","component":"Card","child":"root"}"""),
            update("""{"id":"root","component":"Text","text":"a"},{"id":"root","component":"Text","text":"b"}"""),
            update("""{"id":"a","component":"Card","child":"b"},{"id":"b","component":"Card","child":"a"}"""),
        )
        val session = sampleSession()
        val before = session.surfaces.value
        messages.forEach { message ->
            session.accept(message) shouldBe A2uiResult.Rejected
            session.surfaces.value shouldBe before
        }
    }

    test("bound input size, JSON depth, text length, graph depth and DAG expansion") {
        val tooMany = (0..128).joinToString(",") { """{"id":"n$it","component":"Text","text":"x"}""" }
        val deep = (0..16).joinToString(",") { """{"id":"n$it","component":"Card","child":"n${it + 1}"}""" }
        val dag = (0..8).joinToString(",") { """{"id":"n$it","component":"Column","children":["n${it + 1}","n${it + 1}"]}""" }
        val session = sampleSession()
        val before = session.surfaces.value
        listOf(
            " ".repeat(A2uiSession.MAX_MESSAGE_CHARS + 1),
            "[".repeat(33) + "]".repeat(33),
            update("""{"id":"title","component":"Text","text":"${"x".repeat(4097)}"}"""),
            update(tooMany), update(deep), update(dag),
        ).forEach {
            session.accept(it) shouldBe A2uiResult.Rejected
            session.surfaces.value shouldBe before
        }
    }

    test("bound total surfaces and accumulated components across updates") {
        val session = A2uiSession()
        (0..3).forEach { session.accept(create("", surface = "s$it")) shouldBe A2uiResult.Accepted }
        session.accept(create("", surface = "s4")) shouldBe A2uiResult.Rejected
        val fullSession = A2uiSession()
        val nodes = (0 until 128).joinToString(",") { """{"id":"n$it","component":"Text","text":"x"}""" }
        fullSession.accept(create(nodes)) shouldBe A2uiResult.Accepted
        fullSession.accept(update("""{"id":"extra","component":"Text","text":"x"}""")) shouldBe A2uiResult.Rejected
    }

    test("catalog declares exactly the supported components and no functions") {
        val catalog = Json.parseToJsonElement(requireNotNull(A2uiSessionTest::class.java.getResource("/catalog.json")).readText()).jsonObject
        catalog.getValue("components").jsonObject.keys shouldBe setOf("Text", "Column", "Row", "Card")
        catalog.getValue("functions").jsonObject.isEmpty() shouldBe true
    }
})

private fun sampleSession() = A2uiSession().also {
    it.accept(sampleEnvelope("Title", "Body")) shouldBe A2uiResult.Accepted
}

private fun create(nodes: String, catalog: String = A2uiSession.CATALOG_ID, surface: String = "sample") =
    buildJsonObject {
        put("version", "1.0")
        put("createSurface", buildJsonObject {
            put("surfaceId", surface)
            put("catalogId", catalog)
            if (nodes.isNotEmpty()) put("components", Json.parseToJsonElement("[$nodes]") as JsonArray)
        })
    }.toString()

private fun update(nodes: String) = """{"version":"1.0","updateComponents":{"surfaceId":"sample","components":[$nodes]}}"""
