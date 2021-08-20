/*
 * Designed and developed by 2020 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
