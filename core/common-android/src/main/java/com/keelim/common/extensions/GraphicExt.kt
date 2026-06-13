package com.keelim.common.extensions

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF

class CornerRounding(
    val topLeftRadius: Float = 0f,
    val topRightRadius: Float = 0f,
    val bottomRightRadius: Float = 0f,
    val bottomLeftRadius: Float = 0f,
)

// To FloatArray suitable for Path#addRoundRect
fun CornerRounding.toFloatArray(): FloatArray {
    return floatArrayOf(
        topLeftRadius,
        topLeftRadius,
        topRightRadius,
        topRightRadius,
        bottomRightRadius,
        bottomRightRadius,
        bottomLeftRadius,
        bottomLeftRadius,
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
