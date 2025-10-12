package com.keelim.composeutil.component.shape

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun PreviewPolygon() {
    Image(
        imageVector = Icons.Filled.Check,
        contentDescription = null,
        modifier = Modifier
            .size(100.dp)
            .clip(Polygon(3, 0f)),
    )
}
