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
package com.keelim.composeutil.component.image

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space2
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = Modifier
            .size(144.dp)
            .padding(space8),
        shape = CircleShape,
        border = BorderStroke(1.dp, Color.Black),
        shadowElevation = space4,
    ) {
    }
}

@Preview
@Composable
private fun ProfileImagePreview() {
    ProfileImage()
}

@Composable
fun ProfileDescription() {
    Column(
        modifier = Modifier
            .padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            color = Color.Black,
            fontStyle = FontStyle.Italic,
            fontSize = 24.sp,
            text = "Keelim",
        )

        Text(
            modifier = Modifier.padding(space4),
            text = "Android Developer",
            color = Color.Black,
        )

        Text(
            modifier = Modifier.padding(space2),
            text = "Studying Android, Kotlin, Software",
            color = Color.Black,
        )
    }
}

@Preview
@Composable
private fun ProfileDescriptionPreview() {
    ProfileDescription()
}

@Composable
fun ProfileCard() {
    val buttonClickedState = remember {
        mutableStateOf(false)
    }
    Surface(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Card(
            modifier = Modifier
                .width(144.dp)
                .height(596.dp)
                .padding(space12),
            shape = RoundedCornerShape(corner = CornerSize(space16)),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = space4,
            ),
        ) {
            Column(
                modifier = Modifier.height(300.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ProfileImage()
                HorizontalDivider()
                ProfileDescription()
            }
        }
    }
}

@Preview
@Composable
private fun ProfileCardPreview() {
    ProfileCard()
}
