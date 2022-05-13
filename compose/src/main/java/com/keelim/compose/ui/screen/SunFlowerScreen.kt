package com.keelim.compose.ui.screen

import android.content.res.Configuration
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat


@Preview
@Composable
fun PlantNamePreview() {
    PlantName("Apple")
}

@Composable
fun PlantName(
    name: String,
) {
    Text(
        text = name,
        style = MaterialTheme.typography.h5,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PlantDescPreview() {
    PlantDesc("HTML<br><br>description")
}

@Composable
fun PlantDesc(desc: String) {
    val htmlDesc = remember(desc) {
        HtmlCompat.fromHtml(desc, HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

    AndroidView(factory = { context ->
        TextView(context).apply {
            movementMethod = LinkMovementMethod.getInstance()
        }
    }, update = { textview ->
        textview.text = htmlDesc
    })
}