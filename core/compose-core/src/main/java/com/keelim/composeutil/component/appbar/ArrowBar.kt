package com.keelim.composeutil.component.appbar

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keelim.composeutil.resource.space24
import com.keelim.composeutil.resource.space8

@Composable
fun NavigationBackArrowBar(title: String) {
    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current) {
        "No OnBackPressedDispatcherOwner was provided via LocalOnBackPressedDispatcherOwner"
    }.onBackPressedDispatcher

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = space8)
            .height(50.dp)
            .clickable {
                backDispatcher.onBackPressed()
            },
    ) {
        Icon(
            imageVector = Icons.Rounded.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .size(space24)
                .align(Alignment.CenterVertically),

        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.CenterVertically),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNavigationBackArrowBar() {
    NavigationBackArrowBar(
        "Sample",
    )
}
