package com.keelim.composeutil.component.box

import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun MoveBox(modifier: Modifier = Modifier) {
  val animatedOffset = remember {
    androidx.compose.animation.core.Animatable(Offset(0f, 0f), Offset.VectorConverter)
  }
  Box(
    modifier =
      modifier.fillMaxSize().pointerInput(Unit) {
        coroutineScope {
          while (true) {
            val offset = awaitPointerEventScope { awaitFirstDown().position }
            launch { animatedOffset.animateTo(offset) }
          }
        }
      }
  ) {
    Text("Tab ", Modifier.align(Alignment.Center))
    Box(
      modifier =
        Modifier.offset {
            IntOffset(
              animatedOffset.value.x.roundToInt(),
              animatedOffset.value.y.roundToInt(),
            )
          }
          .size(40.dp)
    )
  }
}

@Preview
@Composable
fun PreviewMoveBox() {
  MoveBox()
}
