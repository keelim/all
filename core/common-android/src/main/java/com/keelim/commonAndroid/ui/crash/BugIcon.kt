package com.keelim.commonAndroid.ui.crash

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp


internal val BugIcon: ImageVector
    get() {
        if (_bugIcon != null) {
            return _bugIcon!!
        }
        _bugIcon = ImageVector.Builder(
            name = "BugIcon",
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
                // Bug body (oval shape)
                moveTo(12f, 8f)
                curveTo(9.5f, 8f, 7.5f, 10f, 7.5f, 12.5f)
                curveTo(7.5f, 15f, 9.5f, 19f, 12f, 19f)
                curveTo(14.5f, 19f, 16.5f, 15f, 16.5f, 12.5f)
                curveTo(16.5f, 10f, 14.5f, 8f, 12f, 8f)
                close()

                // Bug head
                moveTo(12f, 5f)
                curveTo(10.5f, 5f, 9.5f, 6f, 9.5f, 7f)
                curveTo(9.5f, 8f, 10.5f, 8.5f, 12f, 8.5f)
                curveTo(13.5f, 8.5f, 14.5f, 8f, 14.5f, 7f)
                curveTo(14.5f, 6f, 13.5f, 5f, 12f, 5f)
                close()

                // Left antenna
                moveTo(10f, 5.5f)
                lineTo(8f, 3f)
                lineTo(8.5f, 2.5f)
                lineTo(10.5f, 5f)
                close()

                // Right antenna
                moveTo(14f, 5.5f)
                lineTo(16f, 3f)
                lineTo(15.5f, 2.5f)
                lineTo(13.5f, 5f)
                close()

                // Left legs
                moveTo(7.5f, 10f)
                lineTo(5f, 9f)
                lineTo(5f, 9.8f)
                lineTo(7.5f, 10.8f)
                close()

                moveTo(7.5f, 13f)
                lineTo(4.5f, 13f)
                lineTo(4.5f, 13.8f)
                lineTo(7.5f, 13.8f)
                close()

                moveTo(7.5f, 16f)
                lineTo(5f, 17f)
                lineTo(5f, 16.2f)
                lineTo(7.5f, 15.2f)
                close()

                // Right legs
                moveTo(16.5f, 10f)
                lineTo(19f, 9f)
                lineTo(19f, 9.8f)
                lineTo(16.5f, 10.8f)
                close()

                moveTo(16.5f, 13f)
                lineTo(19.5f, 13f)
                lineTo(19.5f, 13.8f)
                lineTo(16.5f, 13.8f)
                close()

                moveTo(16.5f, 16f)
                lineTo(19f, 17f)
                lineTo(19f, 16.2f)
                lineTo(16.5f, 15.2f)
                close()

                // Body segments
                moveTo(8.5f, 11.5f)
                lineTo(15.5f, 11.5f)
                lineTo(15.5f, 12f)
                lineTo(8.5f, 12f)
                close()

                moveTo(8.5f, 14.5f)
                lineTo(15.5f, 14.5f)
                lineTo(15.5f, 15f)
                lineTo(8.5f, 15f)
                close()
            }
        }.build()
        return _bugIcon!!
    }

private var _bugIcon: ImageVector? = null
