package com.keelim.composeutil.component.fab

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun PreviewSubFab() {
    MultiSubFab(
        item = object : FabButtonItem {
            override val imageVector: ImageVector
                get() = Icons.Filled.Add
            override val label: String
                get() = "Add"
        },
        option = FabButtonSub(
            backgroundTint = Color(0xFFE91E63),
            iconTint = Color.White,
        ),
        onClick = {},
    )
}
