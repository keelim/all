package com.keelim.composeutil.demo

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import com.keelim.core.designsystem.component.KuiCard
import com.keelim.core.designsystem.component.KuiHorizontalDivider
import com.keelim.core.designsystem.component.KuiListItem
import com.keelim.core.designsystem.component.KuiSwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import com.keelim.core.designsystem.component.KuiText
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun SwipeToDismiss(
    modifier: Modifier = Modifier,
) {
    val state = rememberSwipeToDismissBoxState()
    KuiSwipeToDismissBox(
        modifier = modifier,
        state = state,
        backgroundContent = {
            val color by animateColorAsState(
                when (state.targetValue) {
                    SwipeToDismissBoxValue.Settled -> Color.LightGray
                    SwipeToDismissBoxValue.StartToEnd -> Color.Green
                    SwipeToDismissBoxValue.EndToStart -> Color.Red
                },
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(color),
            )
        },
    ) {
        KuiCard {
            KuiListItem(
                headlineContent = {
                    KuiText("Cupcake")
                },
                supportingContent = { KuiText("Swipe me left or right!") },
            )
            KuiHorizontalDivider()
        }
    }
}
