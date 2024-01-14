package com.keelim.composeutil.component.button

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ContentBadge(
    modifier : Modifier = Modifier,
    content: @Composable () -> Unit,
    badgeContent: @Composable () -> Unit,
) {
    Box(
        modifier = modifier,
    ) {
        content()
        Badge(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clip(CircleShape),
        ) {
            badgeContent()
        }
    }
}

@Preview
@Composable
fun PreviewContentBadge() {
    ContentBadge(
        modifier = Modifier
            .size(42.dp),
        content = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    modifier = Modifier.size(42.dp),
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "NotificationIcon"
                )
            }
        },
        badgeContent = {
            Text(text = "99+")
        }
    )
}
