package com.keelim.compose.ui.sample

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview

data class Message(val author: String, val writer: String)

@Composable
fun MessageCard(message: Message) {
    Row {
        Image(
            painter = painterResource(id = android.R.drawable.alert_dark_frame),
            contentDescription = "Contact profile picture"
        )

        Column {
            Text(text = message.author)
            Text(text = message.writer)
        }
    }
}

@Preview
@Composable
fun PreviewMessageCard() {
    MessageCard(
        message = Message("keelim", "keelim")
    )
}