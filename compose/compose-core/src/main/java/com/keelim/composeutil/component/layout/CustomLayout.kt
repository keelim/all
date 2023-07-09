package com.keelim.composeutil.component.layout

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout

@Composable
fun CustomLayout(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Layout(
        modifier = modifier,
        content = content,
    ) { measurables, constraints ->
        val looseConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0,
        )
        val placables = measurables.map { measurable ->
            measurable.measure(constraints = looseConstraints)
        }.sortedBy { it.width }

        layout(constraints.maxWidth, constraints.maxHeight) {
            var y = 0
            placables.forEach { placeable ->
                placeable.place(0, y)
                y += placeable.height
            }
        }
    }
}
