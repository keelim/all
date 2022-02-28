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
package com.keelim.cnubus.ui.setting.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keelim.cnubus.utils.toColor

@Composable
internal fun SettingScreen(
    onScreenAction: (ScreenAction) -> Unit,
) {
    Scaffold(
        topBar = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "설정",
                    color = if (isSystemInDarkTheme()) {
                        Color.White
                    } else {
                        Color.Black
                    },
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            CellItem(text = "공지사항") {
                onScreenAction(ScreenAction.Content)
            }
            Divider(
                color = "#EFEFEF".toColor(),
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .height(1.dp)
            )
            CellItem(text = "홈페이지") {
                onScreenAction(ScreenAction.Homepage)
            }
            Divider(
                color = "#EFEFEF".toColor(),
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .height(1.dp)
            )
            CellItem(text = "맵 바로가기") {
                onScreenAction(ScreenAction.Map)
            }
            Divider(
                color = "#EFEFEF".toColor(),
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .height(1.dp)
            )
            CellItem(text = "업데이트") {
                onScreenAction(ScreenAction.Update)
            }
            Divider(
                color = "#EFEFEF".toColor(),
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .height(1.dp)
            )
            CellItem(text = "테마 설정") {
                onScreenAction(ScreenAction.Theme)
            }
            Divider(
                color = "#EFEFEF".toColor(),
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .height(1.dp)
            )
            CellItem(text = "오픈소스 라이선스") {
                onScreenAction(ScreenAction.OpenSource)
            }
            Divider(
                color = "#EFEFEF".toColor(),
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .height(1.dp)
            )
            CellItem(text = "실험 기능") {
                onScreenAction(ScreenAction.Lab)
            }
            Divider(
                color = "#EFEFEF".toColor(),
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .height(1.dp)
            )
            CellItem(text = "근처 지하철") {
                onScreenAction(ScreenAction.Subway)
            }
            Divider(
                color = "#EFEFEF".toColor(),
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .height(1.dp)
            )
            CellItem(text = "개발자 소개") {
                onScreenAction(ScreenAction.Developer)
            }
            Divider(
                color = "#EFEFEF".toColor(),
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .height(1.dp)
            )
            CellItem(text = "실험 기능2") {
                onScreenAction(ScreenAction.Lab2)
            }
            Divider(
                color = "#EFEFEF".toColor(),
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .height(1.dp)
            )
        }
    }
}

@Composable
private fun CellItem(
    text: String,
    onClicked: () -> Unit,
) {
    Text(
        text = text,
        color = if (isSystemInDarkTheme()) {
            Color.White
        } else {
            Color.Black
        },
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 62.dp)
            .clickable { onClicked() }
            .padding(horizontal = 24.dp)
            .wrapContentHeight()
    )
}

@Composable
@Preview
private fun PreviewSettingScreen() {
    SettingScreen(
        onScreenAction = { action ->
        }
    )
}
