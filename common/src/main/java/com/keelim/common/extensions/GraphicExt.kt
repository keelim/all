package com.keelim.common.extensions

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import com.google.android.material.shape.ShapeAppearanceModel

class CornerRounding(
    val topLeftRadius: Float = 0f,
    val topRightRadius: Float = 0f,
    val bottomRightRadius: Float = 0f,
    val bottomLeftRadius: Float = 0f,
)

// To FloatArray suitable for Path#addRoundRect
fun CornerRounding.toFloatArray(): FloatArray {
    return floatArrayOf(
        topLeftRadius, topLeftRadius,
        topRightRadius, topRightRadius,
        bottomRightRadius, bottomRightRadius,
        bottomLeftRadius, bottomLeftRadius
    )
}

fun ShapeAppearanceModel?.toCornerRounding(bounds: RectF): CornerRounding {
    if (this == null) return CornerRounding()
    return CornerRounding(
        topLeftCornerSize.getCornerSize(bounds),
        topRightCornerSize.getCornerSize(bounds),
        bottomRightCornerSize.getCornerSize(bounds),
        bottomLeftCornerSize.getCornerSize(bounds)
    )
}

private val path = Path()
fun Canvas.drawRoundedRect(
    location: RectF,
    cornerRadii: CornerRounding,
    paint: Paint,
) {
    path.apply {
        reset()
        addRoundRect(location, cornerRadii.toFloatArray(), Path.Direction.CW)
    }
    drawPath(path, paint)
}
