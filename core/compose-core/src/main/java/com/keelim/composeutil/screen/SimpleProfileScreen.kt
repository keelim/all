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
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space2
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8

@Composable
fun ImageProfile(modifier: Modifier = Modifier) {
    Surface {
        Image(
            painter = painterResource(id = android.R.drawable.sym_def_app_icon),
            contentDescription = "profile image",
            modifier = modifier.size(135.dp),
            contentScale = ContentScale.Crop,
        )
    }
}

@Preview
@Composable
fun PreviewImageProfile() {
    ImageProfile()
}

@Composable
fun Info() {
    Column {
        Text(
            color = Color.Blue,
            fontSize = 24.sp,
            style = MaterialTheme.typography.bodyLarge,
            text = "Keelim",
        )

        Text(
            text = "Android Developer",
            modifier = Modifier.padding(3.dp),
        )

        Text(
            color = Color.Blue,
            fontSize = 24.sp,
            style = MaterialTheme.typography.bodyLarge,
            text = "Studying Compose",
            modifier = Modifier.padding(3.dp),
        )
    }
}

@Preview
@Composable
fun PreviewInfo() {
    Info()
}

@Composable
fun ProfileCard() {
    val isClicked = remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        Card(
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
                HorizontalDivider()
                Info()
                Button(
                    onClick = {
                        isClicked.value = !isClicked.value
                    },
                ) {
                    Text(
                        text = "Portfolio",
                        style = MaterialTheme.typography.titleSmall,
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

@Preview
@Composable
fun PreviewProfileCard() {
    ProfileCard()
}

@Composable
fun Content() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
    ) {
        Surface(
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
            Card(
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
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(7.dp),
                ) {
                    ImageProfile(modifier = Modifier.size(50.dp))
                    Column(
                        modifier = Modifier
                            .padding(7.dp)
                            .align(alignment = CenterVertically),
                    ) {
                        Text(text = item, fontWeight = FontWeight.Bold)
                        Text(text = "Good Project", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}
