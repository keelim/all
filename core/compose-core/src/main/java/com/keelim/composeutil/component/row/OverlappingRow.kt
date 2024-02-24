package com.keelim.composeutil.component.row

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview

enum class OverlappingState {
    Normal,
    Quarter,
    Half,
    Full
}
@Composable
fun OverlappingRow(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    overlappingState: OverlappingState = OverlappingState.Normal,
) {
    val factor = when(overlappingState) {
        OverlappingState.Normal -> 0.8f
        OverlappingState.Quarter -> 0.75f
        OverlappingState.Half -> 0.5f
        OverlappingState.Full -> 0f
    }
    Layout(
        modifier = modifier,
        content = content,
        measurePolicy = { measurables, constraints ->
            val placeables = measurables.map { it.measure(constraints) }
            val widthsExceptFirst = placeables.subList(1, placeables.size).sumOf { it.width }
            val firstWidth = placeables.getOrNull(0)?.width ?: 0
            val width = (widthsExceptFirst * factor + firstWidth).toInt()
            val height = placeables.maxOf { it.height }
            layout(width, height) {
                var x = 0
                for (placeable in placeables) {
                    placeable.placeRelative(x, 0, 0f)
                    x += (placeable.width * factor).toInt()
                }
            }
        }
    )
}

@Preview
@Composable
fun PreviewOverlappingRow() {
    OverlappingRow(
        content = {
            // Your content here
        }
    )
}
