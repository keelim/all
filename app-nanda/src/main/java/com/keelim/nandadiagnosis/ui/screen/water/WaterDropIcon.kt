package com.keelim.nandadiagnosis.ui.screen.water

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val WaterDropIcon: ImageVector
    get() {
        if (_waterDrop != null) {
            return _waterDrop!!
        }
        _waterDrop = ImageVector.Builder(
            name = "WaterDrop",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
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
                // Water drop shape path
                moveTo(12f, 2f)
                curveTo(12f, 2f, 6f, 8.5f, 6f, 13f)
                curveTo(6f, 16.31f, 8.69f, 19f, 12f, 19f)
                curveTo(15.31f, 19f, 18f, 16.31f, 18f, 13f)
                curveTo(18f, 8.5f, 12f, 2f, 12f, 2f)
                close()
                // Highlight
                moveTo(12f, 17f)
                curveTo(9.79f, 17f, 8f, 15.21f, 8f, 13f)
                curveTo(8f, 11.92f, 8.45f, 10.79f, 9.17f, 9.68f)
                curveTo(9.35f, 9.42f, 9.72f, 9.37f, 9.97f, 9.56f)
                curveTo(10.22f, 9.74f, 10.27f, 10.11f, 10.09f, 10.36f)
                curveTo(9.5f, 11.26f, 9.17f, 12.15f, 9.17f, 13f)
                curveTo(9.17f, 14.56f, 10.44f, 15.83f, 12f, 15.83f)
                curveTo(12.32f, 15.83f, 12.58f, 16.1f, 12.58f, 16.42f)
                curveTo(12.58f, 16.73f, 12.32f, 17f, 12f, 17f)
                close()
            }
        }.build()
        return _waterDrop!!
    }

private var _waterDrop: ImageVector? = null
