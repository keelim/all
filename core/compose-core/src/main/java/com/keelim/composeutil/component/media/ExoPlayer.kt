package com.keelim.composeutil.component.media

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.state.rememberPlayPauseButtonState

@Composable
fun ExoPlayer(
    videoUri: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var showControls by remember { mutableStateOf(true) }

    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .build()
            .also { exoPlayer ->
                MediaItem.Builder()
                    .setUri(videoUri)
                    .build()
                    .also { exoPlayer.setMediaItem(it) }
                exoPlayer.prepare()
            }
    }

    LifecycleResumeEffect(exoPlayer) {
        exoPlayer.play()
        onPauseOrDispose {
            exoPlayer.pause()
        }
    }

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        PlayerSurface(player = exoPlayer)
        if (showControls) {
            PlayPauseButton(exoPlayer)
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun PlayPauseButton(
    player: Player,
    modifier: Modifier = Modifier,
) {
    val state = rememberPlayPauseButtonState(player)
    val icon = if (state.showPlay) Icons.Default.PlayArrow else Icons.Rounded.PlayArrow

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val graySemiTransparentBG = Color.Gray.copy(alpha = 0.1f)
        IconButton(
            onClick = state::onClick,
            modifier = Modifier
                .size(100.dp)
                .background(graySemiTransparentBG, CircleShape),
            enabled = state.isEnabled,
        ) {
            Icon(icon, contentDescription = null)
        }
    }
}
