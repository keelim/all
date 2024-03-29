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
package com.keelim.composeutil.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space24

@Composable
fun MyDialog(
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties,
    ) {
        content()
    }
}

@Composable
fun NoticeDialog(
    visibility: Boolean,
    onDismissRequest: () -> Unit,
) {
    if (visibility) {
        MyDialog(onDismissRequest = onDismissRequest) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(space12))
                    .background(Color.White),
            ) {
                Text(
                    modifier = Modifier
                        .padding(top = space24)
                        .padding(horizontal = space24),
                    text = "알림 다이알로그 입니다.",
                )
                Text(
                    modifier = Modifier
                        .padding(space12)
                        .align(Alignment.End)
                        .clickable {
                            onDismissRequest.invoke()
                        }
                        .padding(space12),
                    text = "OK",
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewNoticeDialog() {
    NoticeDialog(visibility = true) {
    }
}
