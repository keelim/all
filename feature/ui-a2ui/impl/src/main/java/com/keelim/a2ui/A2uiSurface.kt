package com.keelim.a2ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.keelim.core.designsystem.component.KuiPanel
import com.keelim.core.designsystem.theme.KuiTheme

/** Render a validated snapshot within the host's theme and scroll container. */
@Composable
fun A2uiSurface(document: A2uiDocument, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        AnimatedContent(targetState = document, label = "A2UI surface") { snapshot ->
            RenderNode(snapshot, "root")
        }
    }
}

@Composable
private fun RenderNode(document: A2uiDocument, id: String) {
    when (val node = document.nodes[id]) {
        is A2uiNode.Text -> Text(
            text = node.text,
            style = KuiTheme.typography.bodyLarge,
            color = KuiTheme.colorScheme.onSurface,
        )
        is A2uiNode.Column -> Column(verticalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space2)) {
            node.children.forEachIndexed { index, child -> key(index, child) { RenderNode(document, child) } }
        }
        is A2uiNode.Row -> Row(horizontalArrangement = Arrangement.spacedBy(KuiTheme.spacing.space2)) {
            node.children.forEachIndexed { index, child -> key(index, child) { RenderNode(document, child) } }
        }
        is A2uiNode.Card -> KuiPanel { RenderNode(document, node.child) }
        null -> Text(
            text = stringResource(R.string.a2ui_waiting_for_component),
            style = KuiTheme.typography.bodyMedium,
            color = KuiTheme.colorScheme.onSurfaceVariant,
        )
    }
}
