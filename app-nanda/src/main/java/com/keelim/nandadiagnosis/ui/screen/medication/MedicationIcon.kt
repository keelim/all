package com.keelim.nandadiagnosis.ui.screen.medication

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val MedicationIcon: ImageVector
    get() {
        if (_medication != null) {
            return _medication!!
        }
        _medication = ImageVector.Builder(
            name = "Medication",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            // Pill/Capsule shape
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero,
            ) {
                // Capsule body - left half
                moveTo(4.22f, 11.22f)
                lineTo(11.22f, 4.22f)
                curveTo(12.6f, 2.84f, 14.81f, 2.84f, 16.19f, 4.22f)
                lineTo(19.78f, 7.81f)
                curveTo(21.16f, 9.19f, 21.16f, 11.4f, 19.78f, 12.78f)
                lineTo(12.78f, 19.78f)
                curveTo(11.4f, 21.16f, 9.19f, 21.16f, 7.81f, 19.78f)
                lineTo(4.22f, 16.19f)
                curveTo(2.84f, 14.81f, 2.84f, 12.6f, 4.22f, 11.22f)
                close()
                
                // Dividing line in middle
                moveTo(9.17f, 16.17f)
                lineTo(15.17f, 10.17f)
                lineTo(13.83f, 8.83f)
                lineTo(7.83f, 14.83f)
                lineTo(9.17f, 16.17f)
                close()
            }
        }.build()
        return _medication!!
    }

private var _medication: ImageVector? = null
