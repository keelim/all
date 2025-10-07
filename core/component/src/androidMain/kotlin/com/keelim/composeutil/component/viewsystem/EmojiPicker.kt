package com.keelim.composeutil.component.viewsystem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.emoji2.emojipicker.EmojiPickerView
import androidx.emoji2.emojipicker.EmojiViewItem

@Composable
fun EmojiPickerView() {
    var emoji by remember { mutableStateOf(EmojiViewItem("😀", emptyList())) }
    Column {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = {
                EmojiPickerView(it).apply {
                    emojiGridColumns = 9
                    emojiGridRows = 6f
                    // set pick listener
                    setOnEmojiPickedListener { item -> emoji = item }
                }
            },
        )
        Text(text = emoji.emoji)
    }
}
