package com.keelim.composeutil.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.keelim.composeutil.resource.space24
import com.keelim.composeutil.resource.space8
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SelectableButton(modifier: Modifier = Modifier, isSelected: Boolean = false) {
    val (icon, iconColor) = if (isSelected) {
        Icons.Filled.Done to MaterialTheme.colorScheme.onPrimary
    } else {
        Icons.Filled.Add to MaterialTheme.colorScheme.primary
    }
    val (borderColor, backgroundColor) = if (isSelected) {
        MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f) to MaterialTheme.colorScheme.onPrimary
    }
    Surface(
        modifier = modifier.size(36.dp, 36.dp),
        color = backgroundColor,
        shape = CircleShape,
        border = BorderStroke(1.dp, borderColor),
    ) {
        Image(
            modifier = Modifier.padding(space8),
            imageVector = icon,
            colorFilter = ColorFilter.tint(iconColor),
            contentDescription = null,
        )
    }
}

@Preview
@Composable
fun PreviewSelectedButton() {
    SelectableButton(modifier = Modifier.size(space24), isSelected = true)
}

@Preview
@Composable
fun PreviewUnSelectedButton() {
    SelectableButton(modifier = Modifier.size(space24), isSelected = false)
}
