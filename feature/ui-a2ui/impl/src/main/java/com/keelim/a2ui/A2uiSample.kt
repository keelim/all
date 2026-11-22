package com.keelim.a2ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.keelim.core.designsystem.theme.KeelimDesignSystemTheme
import com.keelim.core.designsystem.theme.KuiTheme
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/** Offline sample; no transport, credentials, model calls, or app navigation registration. */
@Composable
fun A2uiSample(modifier: Modifier = Modifier) {
    val title = stringResource(R.string.a2ui_sample_title)
    val body = stringResource(R.string.a2ui_sample_body)
    val document = remember(title, body) {
        val session = A2uiSession()
        check(session.accept(sampleEnvelope(title, body)) == A2uiResult.Accepted) { "Invalid bundled sample" }
        session.surfaces.value.getValue("sample")
    }
    A2uiSurface(document = document, modifier = modifier.padding(KuiTheme.spacing.space4))
}

internal fun sampleEnvelope(title: String, body: String): String = buildJsonObject {
    put("version", "1.0")
    put("createSurface", buildJsonObject {
        put("surfaceId", "sample")
        put("catalogId", A2uiSession.CATALOG_ID)
        put("components", JsonArray(listOf(
            buildJsonObject {
                put("id", "root")
                put("component", "Card")
                put("child", "content")
            },
            buildJsonObject {
                put("id", "content")
                put("component", "Column")
                put("children", JsonArray(listOf(JsonPrimitive("title"), JsonPrimitive("body"))))
            },
            buildJsonObject {
                put("id", "title")
                put("component", "Text")
                put("text", title)
            },
            buildJsonObject {
                put("id", "body")
                put("component", "Text")
                put("text", body)
            },
        )))
    })
}.toString()

@Preview
@Composable
private fun A2uiSamplePreview() {
    KeelimDesignSystemTheme { A2uiSample() }
}
