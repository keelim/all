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
package com.keelim.nandadiagnosis.compose.ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.keelim.nandadiagnosis.compose.R

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ProfileImageView(
  imageUrl: String,
  stateColor: Color,
  modifier: Modifier = Modifier,
  builder: ImageRequest.Builder.() -> Unit = {
    crossfade(true)
    placeholder(R.drawable.sample)
    error(R.drawable.sample)
  },
) {
  val painter = rememberImagePainter(
    data = imageUrl,
    builder = builder,
  )

  val filter = when (painter.state) {
    is ImagePainter.State.Success -> null
    else -> ColorFilter.tint(stateColor)
  }

  Image(
    modifier = modifier,
    painter = painter,
    colorFilter = filter,
    contentDescription = null,
  )
}
