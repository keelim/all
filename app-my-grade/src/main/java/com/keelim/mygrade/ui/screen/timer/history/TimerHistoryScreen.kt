package com.keelim.mygrade.ui.screen.timer.history
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.keelim.composeutil.component.box.ReadyServiceBox

@Composable
fun TimerHistoryRoute() {
    TimerHistoryScreen()
}

@Composable
fun TimerHistoryScreen(viewModel: TimerHistoryViewModel = hiltViewModel()) {
    ReadyServiceBox()
}

@Preview(showBackground = true)
@Composable
private fun PreviewTimerHistoryScreen() {
    TimerHistoryScreen()
}
