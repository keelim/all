package com.keelim.composeutil.component.spacer

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.keelim.composeutil.resource.space8

@Composable
fun VerticalSpacer(height: Dp = space8) {
    Spacer(modifier = Modifier.height(height))
}

@Composable
fun HorizontalSpacer(width: Dp = space8) {
    Spacer(modifier = Modifier.width(width))
}
