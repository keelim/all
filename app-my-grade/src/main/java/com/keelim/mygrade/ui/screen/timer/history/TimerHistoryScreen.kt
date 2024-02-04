package com.keelim.mygrade.ui.screen.timer.history

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tracing.trace
import com.keelim.composeutil.component.box.ReadyServiceBox

@Composable
fun TimerHistoryRoute() = trace("TimerHistoryRoute") {
    TimerHistoryScreen()
}

@Composable
fun TimerHistoryScreen(viewModel: TimerHistoryViewModel = hiltViewModel()) = trace("TimerHistoryScreen") {
    ReadyServiceBox()
}

@Preview(showBackground = true)
@Composable
fun PreviewTimerHistoryScreen() {
    TimerHistoryScreen()
}
