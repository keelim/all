package com.keelim.arducon.ui.screen.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.keelim.composeutil.resource.space2
import com.keelim.composeutil.resource.space8
import com.keelim.core.database.model.DeepLink

@Composable
fun DeepLinkSection(
    items: List<DeepLink>,
    onSearch: (String) -> Unit,
    onDelete: (DeepLink) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space8),
    ) {
        items(
            items = items,
        ) {
            DeepLinkItem(
                deepLink = it,
                onSearch = onSearch,
                onDelete = onDelete,
            )
        }
    }
}

@Composable
private fun DeepLinkItem(
    deepLink: DeepLink,
    onSearch: (String) -> Unit,
    onDelete: (DeepLink) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(space8),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = deepLink.url,
                modifier = Modifier.weight(1f),
            )
            Spacer(
                modifier = Modifier.width(space8),
            )
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "play",
                modifier = Modifier.clickable { onSearch(deepLink.url) },
            )
            Spacer(
                modifier = Modifier.width(space2),
            )
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "delete",
                modifier = Modifier.clickable { onDelete(deepLink) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDeepLinkSection() {
    DeepLinkSection(
        items = listOf(
            DeepLink(
                url = "https://www.google.com",
                timestamp = 2323L,
            ),
            DeepLink(
                url = "https://www.google.com",
                timestamp = 2323L,
            ),
            DeepLink(
                url = "https://www.google.com",
                timestamp = 2323L,
            ),
            DeepLink(
                url = "https://www.google.com",
                timestamp = 2323L,
            ),
        ),
        onSearch = {},
        onDelete = {},
    )
}
