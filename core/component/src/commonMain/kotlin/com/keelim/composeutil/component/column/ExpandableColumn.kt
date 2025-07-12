package com.keelim.composeutil.component.column

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun ExpandableColumn(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .clickable { isExpanded = !isExpanded }
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth(),
    ) {
        Text(
            text = title,
        )
        AnimatedVisibility(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .fillMaxWidth(),
            visible = isExpanded,
        ) {
            content()
        }
    }
}
