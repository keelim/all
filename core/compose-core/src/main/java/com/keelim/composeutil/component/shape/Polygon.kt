package com.keelim.composeutil.component.shape

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

class Polygon(val slides: Int, val rotation: Float = 0f) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline = Outline.Generic(
        path = Path().apply {
            val radius = if (size.width > size.height) size.width else size.height
            val angle = 2.0 * Math.PI / slides
            val cx = size.width / 2f
            val cy = size.height / 2f
            val r = rotation * (Math.PI / 180)
            moveTo(
                cx + (radius * cos(0.0 + r).toFloat()),
                cy + (radius + sin(0.0 + r).toFloat()),
            )
            for (i in 1 until slides) {
                lineTo(
                    cx + (radius * cos(angle + r).toFloat()),
                    cy + (radius + sin(angle + r).toFloat()),
                )
            }
            close()
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewPolygon() {
    Image(
        imageVector = Icons.Filled.Check,
        contentDescription = null,
        modifier = Modifier
            .size(100.dp)
            .clip(Polygon(3, 0f)),
    )
}
