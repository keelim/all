import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.keelim.mygrade.ui.screen.timer.history.TimerHistoryViewModel

@Composable
fun TimerHistoryRoute() {
    TimerHistoryScreen()
}

@Composable
fun TimerHistoryScreen(
    viewModel: TimerHistoryViewModel = hiltViewModel()
) {

}

@Preview(showBackground = true)
@Composable
private fun PreviewTimerHistoryScreen() {
    TimerHistoryScreen()

}
