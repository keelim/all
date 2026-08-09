package com.keelim.composeutil.component.appbar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import com.keelim.composeutil.component.kui.KuiIcon
import com.keelim.composeutil.component.kui.KuiMaterialTheme
import com.keelim.composeutil.component.kui.KuiText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8

@Composable
fun MultiAppBar(title: String) {
    Row(
        modifier = Modifier.padding(horizontal = space4, vertical = space8),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        KuiText(text = title, style = KuiMaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.weight(1f))
        KuiIcon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = null,
        )
    }
}
