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
import androidx.compose.ui.unit.dp

@Composable
fun MultiAppBar(title: String) {
    Row(
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
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
private fun PreviewMultiAppBar() {
    MultiAppBar("App Name")
}
