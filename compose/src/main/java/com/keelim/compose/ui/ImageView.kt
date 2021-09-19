package com.keelim.compose.ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.keelim.compose.R

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