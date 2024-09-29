package com.keelim.setting.screen.alarm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.trace
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.composeutil.component.layout.Loading
import com.keelim.model.Alarm

@Composable
fun AlarmRoute(
    viewModel: AlarmViewModel = hiltViewModel(),
) = trace("AlarmRoute") {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    AlarmScreen(screenState = screenState)
}

@Composable
fun AlarmScreen(
    screenState: SealedUiState<List<Alarm>>,
) = trace("AlarmScreen") {
    when (screenState) {
        is SealedUiState.Loading, is SealedUiState.Error -> Loading()
        is SealedUiState.Success -> AlarmSection(screenState.value)
    }
}

@Preview
@Composable
private fun PreviewAlarmScreen() {
    AlarmScreen(
        screenState = SealedUiState.Success(
            value = listOf(
                Alarm(
                    title = "propriae", subTitle = "no", receiveDate = "commune"
                )
            )
        ),
    )
}

