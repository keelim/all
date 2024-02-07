package com.keelim.composeutil.component.appbar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.keelim.composeutil.resource.space4

@Composable
fun MultiAppBar(title: String) {
    Row(
        modifier = Modifier.padding(horizontal = space4, vertical = space8),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = null,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMultiAppBar() {
    MultiAppBar("App Name")
}
