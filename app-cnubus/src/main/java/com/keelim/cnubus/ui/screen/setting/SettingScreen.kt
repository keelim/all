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
@file:OptIn(ExperimentalMaterial3Api::class)

package com.keelim.cnubus.ui.screen.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.trace
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space24
import com.keelim.composeutil.resource.space8

private val settings by lazy {
    val data = listOf(
        Setting(
            text = "앱 설정",
            action = ScreenAction.AppSetting,
        ),
        Setting(
            text = "홈페이지",
            action = ScreenAction.Homepage,
        ),
        Setting(
            text = "맵 바로가기",
            action = ScreenAction.Map,
        ),
    )
    data
}

@Composable
internal fun SettingScreen(
    onScreenAction: (ScreenAction) -> Unit,
) = trace("SettingScreen") {
    Scaffold(
        topBar = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(space24),
            ) {
                Text(
                    text = "설정",
                    color = if (isSystemInDarkTheme()) {
                        Color.White
                    } else {
                        Color.Black
                    },
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        },
    ) { innerPadding ->
        val data = remember { settings }
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
        ) {
            items(
                items = data,
                itemContent = {
                    SettingItem(item = it, onScreenAction)
                },
            )
        }
    }
}

@Stable
data class Setting(
    val text: String,
    val action: ScreenAction,
)

@Composable
fun SettingItem(
    item: Setting,
    onScreenAction: (ScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) = trace("SettingItem") {
    Card(
        modifier = modifier
            .padding(
                horizontal = space8,
                vertical = space8,
            )
            .fillMaxWidth(),
    ) {
        Row {
            Column(
                modifier = Modifier
                    .padding(space16)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically),
            ) {
                CellItem(
                    text = item.text,
                    onClick = { onScreenAction(item.action) },
                )
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun CellItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) = trace("CellItem") {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 62.dp)
            .clickable { onClick() }
            .padding(horizontal = space24)
            .wrapContentHeight(),
    )
}

@Composable
@Preview
fun PreviewSettingScreen() {
    SettingScreen(
        onScreenAction = {},
    )
}
