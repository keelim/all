package com.keelim.arducon.ui.screen.search

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.keelim.model.DeepLink

@Composable
fun DeepLinkSearchSection(
    items: List<DeepLink>,
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
    ) {
        items(items) { items ->
            Text(
                text = items.url,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
