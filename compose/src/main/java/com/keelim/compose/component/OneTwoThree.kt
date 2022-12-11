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
package com.keelim.compose.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun OneTwoThreeRow() {
    Row {
        Text(
            text = "One",
            fontSize = 48.sp,
        )
        Text(
            text = "Two",
            fontSize = 48.sp,
        )
        Text(
            text = "Three",
            fontSize = 48.sp,
        )
    }
}

@Preview
@Composable
fun OneTwoThreeRow2() {
    Row(horizontalArrangement = Arrangement.Center) {
        Text(
            text = "One",
            fontSize = 48.sp,
        )
        Text(
            text = "Two",
            fontSize = 48.sp,
        )
        Text(
            text = "Three",
            fontSize = 48.sp,
        )
    }
}

@Preview
@Composable
fun OneTwoThreeColumn() {
    Column {
        Text(
            text = "One",
            fontSize = 48.sp,
        )
        Text(
            text = "Two",
            fontSize = 48.sp,
        )
        Text(
            text = "Three",
            fontSize = 48.sp,
        )
    }
}

@Preview
@Composable
fun OneTwoThreeColumn2() {
    Column(verticalArrangement = Arrangement.Center) {
        Text(
            text = "One",
            fontSize = 48.sp,
        )
        Text(
            text = "Two",
            fontSize = 48.sp,
        )
        Text(
            text = "Three",
            fontSize = 48.sp,
        )
    }
}
