package com.keelim.mygrade.ui.screen.timer.history

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.trace
import androidx.hilt.navigation.compose.hiltViewModel
import com.keelim.composeutil.component.box.ReadyServiceBox

@Composable
fun TimerHistoryRoute(
    viewModel: TimerHistoryViewModel = hiltViewModel(),
) = trace("TimerHistoryRoute") {
    TimerHistoryScreen()
}

@Composable
fun TimerHistoryScreen() = trace("TimerHistoryScreen") {
    ReadyServiceBox()
}

@Preview(showBackground = true)
@Composable
fun PreviewTimerHistoryScreen() {
    TimerHistoryScreen()
}
