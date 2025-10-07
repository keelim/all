package com.keelim.composeutil.component.shape

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class Polygon(val slides: Int, val rotation: Float = 0f) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline = Outline.Generic(
        path = Path(),
        // .apply {
        // val radius = if (size.width > size.height) size.width else size.height
        // val angle = 2.0 * Math.PI / slides
        // val cx = size.width / 2f
        // val cy = size.height / 2f
        // val r = rotation * (Math.PI / 180)
        // moveTo(
        //     cx + (radius * cos(0.0 + r).toFloat()),
        //     cy + (radius + sin(0.0 + r).toFloat()),
        // )
        // for (i in 1 until slides) {
        //     lineTo(
        //         cx + (radius * cos(angle + r).toFloat()),
        //         cy + (radius + sin(angle + r).toFloat()),
        //     )
        // }
        // close()
        // },
    )
}
