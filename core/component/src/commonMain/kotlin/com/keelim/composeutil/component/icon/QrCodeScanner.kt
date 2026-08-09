package com.keelim.composeutil.component.icon

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Composable
fun rememberQrCodeScanner(
    tintColor: Color = Color.Black,
): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "qr_code_scanner",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f,
        ).apply {
            path(
                fill = SolidColor(tintColor),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero,
            ) {
                moveTo(4.917f, 11.042f)
                quadToRelative(-0.542f, 0f, -0.917f, -0.375f)
                reflectiveQuadToRelative(-0.375f, -0.959f)
                verticalLineTo(4.917f)
                quadToRelative(0f, -0.584f, 0.354f, -0.938f)
                reflectiveQuadToRelative(0.938f, -0.354f)
                horizontalLineTo(9.75f)
                quadToRelative(0.542f, 0f, 0.917f, 0.375f)
                reflectiveQuadToRelative(0.375f, 0.917f)
                quadToRelative(0f, 0.583f, -0.375f, 0.958f)
                reflectiveQuadToRelative(-0.917f, 0.375f)
                horizontalLineToRelative(-3.5f)
                verticalLineToRelative(3.458f)
                quadToRelative(0f, 0.584f, -0.396f, 0.959f)
                reflectiveQuadToRelative(-0.937f, 0.375f)
                close()
                moveToRelative(0f, 25.333f)
                quadToRelative(-0.584f, 0f, -0.938f, -0.354f)
                reflectiveQuadToRelative(-0.354f, -0.938f)
                verticalLineTo(30.25f)
                quadToRelative(0f, -0.542f, 0.375f, -0.917f)
                reflectiveQuadToRelative(0.917f, -0.375f)
                quadToRelative(0.583f, 0f, 0.958f, 0.375f)
                reflectiveQuadToRelative(0.375f, 0.917f)
                verticalLineToRelative(3.5f)
                horizontalLineToRelative(3.5f)
                quadToRelative(0.542f, 0f, 0.917f, 0.396f)
                reflectiveQuadToRelative(0.375f, 0.937f)
                quadToRelative(0f, 0.542f, -0.375f, 0.917f)
                reflectiveQuadToRelative(-0.917f, 0.375f)
                close()
                moveToRelative(25.375f, 0f)
                quadToRelative(-0.584f, 0f, -0.959f, -0.375f)
                reflectiveQuadToRelative(-0.375f, -0.917f)
                quadToRelative(0f, -0.583f, 0.375f, -0.958f)
                reflectiveQuadToRelative(0.959f, -0.375f)
                horizontalLineToRelative(3.458f)
                verticalLineToRelative(-3.5f)
                quadToRelative(0f, -0.542f, 0.396f, -0.917f)
                reflectiveQuadToRelative(0.937f, -0.375f)
                quadToRelative(0.542f, 0f, 0.917f, 0.375f)
                reflectiveQuadToRelative(0.375f, 0.917f)
                verticalLineToRelative(4.833f)
                quadToRelative(0f, 0.584f, -0.354f, 0.938f)
                reflectiveQuadToRelative(-0.938f, 0.354f)
                close()
                moveToRelative(4.791f, -25.333f)
                quadToRelative(-0.583f, 0f, -0.958f, -0.375f)
                reflectiveQuadToRelative(-0.375f, -0.959f)
                verticalLineTo(6.25f)
                horizontalLineToRelative(-3.458f)
                quadToRelative(-0.584f, 0f, -0.959f, -0.396f)
                reflectiveQuadToRelative(-0.375f, -0.937f)
                quadToRelative(0f, -0.542f, 0.375f, -0.917f)
                reflectiveQuadToRelative(0.959f, -0.375f)
                horizontalLineToRelative(4.791f)
                quadToRelative(0.584f, 0f, 0.938f, 0.354f)
                reflectiveQuadToRelative(0.354f, 0.938f)
                verticalLineToRelative(4.791f)
                quadToRelative(0f, 0.584f, -0.375f, 0.959f)
                reflectiveQuadToRelative(-0.917f, 0.375f)
                close()
                moveTo(29.208f, 29.25f)
                horizontalLineToRelative(2.5f)
                verticalLineToRelative(2.5f)
                horizontalLineToRelative(-2.5f)
                close()
                moveToRelative(0f, -5f)
                horizontalLineToRelative(2.5f)
                verticalLineToRelative(2.5f)
                horizontalLineToRelative(-2.5f)
                close()
                moveToRelative(-2.5f, 2.5f)
                horizontalLineToRelative(2.5f)
                verticalLineToRelative(2.5f)
                horizontalLineToRelative(-2.5f)
                close()
                moveToRelative(-2.5f, 2.5f)
                horizontalLineToRelative(2.5f)
                verticalLineToRelative(2.5f)
                horizontalLineToRelative(-2.5f)
                close()
                moveToRelative(-2.5f, -2.5f)
                horizontalLineToRelative(2.5f)
                verticalLineToRelative(2.5f)
                horizontalLineToRelative(-2.5f)
                close()
                moveToRelative(5f, -5f)
                horizontalLineToRelative(2.5f)
                verticalLineToRelative(2.5f)
                horizontalLineToRelative(-2.5f)
                close()
                moveToRelative(-2.5f, 2.5f)
                horizontalLineToRelative(2.5f)
                verticalLineToRelative(2.5f)
                horizontalLineToRelative(-2.5f)
                close()
                moveToRelative(-2.5f, -2.5f)
                horizontalLineToRelative(2.5f)
                verticalLineToRelative(2.5f)
                horizontalLineToRelative(-2.5f)
                close()
                moveToRelative(10f, -13.5f)
                verticalLineToRelative(10f)
                horizontalLineToRelative(-10f)
                verticalLineToRelative(-10f)
                close()
                moveTo(18.25f, 21.75f)
                verticalLineToRelative(10f)
                horizontalLineToRelative(-10f)
                verticalLineToRelative(-10f)
                close()
                moveToRelative(0f, -13.5f)
                verticalLineToRelative(10f)
                horizontalLineToRelative(-10f)
                verticalLineToRelative(-10f)
                close()
                moveToRelative(-2.125f, 21.375f)
                verticalLineToRelative(-5.75f)
                horizontalLineToRelative(-5.75f)
                verticalLineToRelative(5.75f)
                close()
                moveToRelative(0f, -13.5f)
                verticalLineToRelative(-5.75f)
                horizontalLineToRelative(-5.75f)
                verticalLineToRelative(5.75f)
                close()
                moveToRelative(13.458f, 0f)
                verticalLineToRelative(-5.75f)
                horizontalLineToRelative(-5.75f)
                verticalLineToRelative(5.75f)
                close()
            }
        }.build()
    }
}
