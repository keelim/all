package com.keelim.composeutil.component.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Preview
@Composable
fun PreviewCircularLayout() {
    CircularLayout {
        (0..10).map {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(
                        color = Color(
                            red = Random.nextInt(255),
                            green = Random.nextInt(255),
                            blue = Random.nextInt(255),
                        ),
                        shape = CircleShape,
                    ),
            )
        }
    }
}

