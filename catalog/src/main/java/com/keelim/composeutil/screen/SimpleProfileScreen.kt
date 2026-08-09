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

package com.keelim.composeutil.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import com.keelim.core.designsystem.component.KuiButton
import com.keelim.core.designsystem.component.KuiCard
import androidx.compose.material3.CardDefaults
import com.keelim.core.designsystem.component.KuiHorizontalDivider
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.designsystem.component.KuiSurface
import com.keelim.core.designsystem.component.KuiText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space2
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8

@Composable
fun ImageProfile(modifier: Modifier = Modifier) {
    KuiSurface {
    }
}

@Composable
fun Info() {
    Column {
        KuiText(
            color = Color.Blue,
            fontSize = 24.sp,
            style = KuiTheme.typography.bodyLarge,
            text = "Keelim",
        )

        KuiText(
            text = "Android Developer",
            modifier = Modifier.padding(3.dp),
        )

        KuiText(
            color = Color.Blue,
            fontSize = 24.sp,
            style = KuiTheme.typography.bodyLarge,
            text = "Studying Compose",
            modifier = Modifier.padding(3.dp),
        )
    }
}

@Composable
fun ProfileCard() {
    val isClicked = remember { mutableStateOf(false) }
    KuiSurface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        KuiCard(padded = false,
            modifier = Modifier
                .width(200.dp)
                .height(390.dp)
                .padding(space12),
            shape = RoundedCornerShape(corner = CornerSize(15.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = space4,
            ),
        ) {
            Column(
                modifier = Modifier.height(300.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ImageProfile()
                KuiHorizontalDivider()
                Info()
                KuiButton(
                    onClick = {
                        isClicked.value = !isClicked.value
                    },
                ) {
                    KuiText(
                        text = "Portfolio",
                        style = KuiTheme.typography.titleSmall,
                    )
                }
                if (isClicked.value) {
                    Content()
                } else {
                    Box {}
                }
            }
        }
    }
}

@Composable
fun Content() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
    ) {
        KuiSurface(
            modifier = Modifier
                .padding(3.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            shape = RoundedCornerShape(corner = CornerSize(6.dp)),
            border = BorderStroke(
                width = space2,
                color = Color.LightGray,
            ),
        ) {
            Portfolio(
                data = listOf(
                    "Project1",
                    "Project1",
                    "Project1",
                    "Project1",
                    "Project1",
                ),
            )
        }
    }
}

@Composable
fun Portfolio(data: List<String>) {
    LazyColumn {
        items(data) { item ->
            KuiCard(padded = false,
                modifier = Modifier
                    .padding(13.dp)
                    .fillMaxWidth(),
                shape = RectangleShape,
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = space4,
                ),
            ) {
                Row(
                    modifier = Modifier
                        .padding(space8)
                        .background(KuiTheme.colorScheme.surface)
                        .padding(7.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .padding(7.dp)
                            .align(alignment = CenterVertically),
                    ) {
                        KuiText(text = item, fontWeight = FontWeight.Bold)
                        KuiText(text = "Good Project", style = KuiTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}
