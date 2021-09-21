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
package com.keelim.ui

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun SettingScreen(
  onScreenAction: (Section) -> Unit,
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
          color = Color.Black,
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }
  ) { innerPadding ->
    Column(Modifier.padding(innerPadding)) {
      CellItem("Speaker") {
        onScreenAction(Section.Developer)
      }
      Divider(
        color = Color.Black,
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
  onClicked: () -> Unit
) {
  Text(
    text = text,
    color = Color.Black,
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
