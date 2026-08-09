package com.keelim.composeutil.component.button

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import com.keelim.composeutil.component.kui.KuiBadge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

@Composable
fun ContentBadge(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    badgeContent: @Composable () -> Unit,
) {
    Box(
        modifier = modifier,
    ) {
        content()
        KuiBadge(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clip(CircleShape),
        ) {
            badgeContent()
        }
    }
}
