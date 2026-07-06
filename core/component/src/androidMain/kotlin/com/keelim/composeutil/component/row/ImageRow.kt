package com.keelim.composeutil.component.row

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.designsystem.component.KuiSurface
import com.keelim.core.designsystem.component.KuiText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.keelim.composeutil.resource.space16

@Composable
fun ImageLabelRow(
    imageUrl: String,
    label: String,
    modifier: Modifier = Modifier,
) {
    KuiSurface(
        shape = KuiTheme.shapes.medium,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(80.dp),
            )
            KuiText(
                text = label,
                modifier = Modifier.padding(horizontal = space16),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewImageLabelRow() {
    ImageLabelRow(
        imageUrl = "http://www.bing.com/search?q=reprimique",
        label = "rhoncus",
    )
}
