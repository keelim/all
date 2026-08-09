package com.keelim.composeutil.component.box

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import com.keelim.core.designsystem.component.KuiText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ReadyServiceBox(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        KuiText(
            text = "현재 서비스 준비 중입니다.",
            modifier = Modifier.align(Alignment.Center),
        )
    }
}
