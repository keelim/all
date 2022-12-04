/*
 * Designed and developed by 2021 keelim (Jaehyun Kim)
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
package com.keelim.cnubus.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.keelim.data.model.File

@Composable
fun ItemFile(
    file: File,
    startDownload: (File) -> Unit,
    openFile: (File) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(width = 2.dp, color = Color.Blue, shape = RoundedCornerShape(16.dp))
            .clickable {
                if (!file.isDownloading) {
                    if (file.downloadedUri.isNullOrEmpty()) {
                        startDownload(file)
                    } else {
                        openFile(file)
                    }
                }
            }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
            ) {
                Text(
                    text = file.name,
                    color = Color.Black
                )

                Row {
                    val description = if (file.isDownloading) {
                        "Downloading..."
                    } else {
                        if (file.downloadedUri.isNullOrEmpty()) "Tap to download the file" else "Tap to open file"
                    }
                    Text(
                        text = description,
                        color = Color.DarkGray
                    )
                }
                if (file.isDownloading) {
                    CircularProgressIndicator(
                        color = Color.Blue,
                        modifier = Modifier
                            .size(32.dp)
                    )
                }
            }
        }
    }
}
