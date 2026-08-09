package com.keelim.composeutil.component.custom

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import com.keelim.core.designsystem.component.KuiButton
import com.keelim.core.designsystem.component.KuiText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keelim.composeutil.resource.space24

@Preview
@Composable
fun HeartScreen() {
    val heartCount = remember { mutableIntStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        repeat(heartCount.intValue) {
            Heart(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 36.dp),
                horizontalPadding = 24,
                bottomMargin = 110,
                width = 360,
                height = 640,
            )
        }

        KuiButton(
            onClick = {
                heartCount.intValue++
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(space24)
                .wrapContentHeight()
                .wrapContentWidth(),
        ) {
            KuiText(
                text = "Like",
                color = Color.White,
            )
        }
    }
}
