package com.keelim.comssa.ui.screen.ecocal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.composeutil.component.layout.EmptyView
import com.keelim.data.source.firebase.EcoCalEntry
import com.keelim.data.source.firebase.EcocalEntries

@Composable
fun EcocalRoute(viewModel: EcocalViewModel = hiltViewModel()) {
    val uiState by viewModel.ref.collectAsStateWithLifecycle()
    EcocalScreen(uiState)
}

@Composable
fun EcocalScreen(state: SealedUiState<EcocalEntries>) {
    when (state) {
        is SealedUiState.Error,
        SealedUiState.Loading,
        -> EmptyView()
        is SealedUiState.Success -> EcocalMainSection(state.value)
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewEcocalScreen() {
    EcocalScreen(
        state =
        SealedUiState.success(
            EcocalEntries(
                entries =
                listOf(
                    EcoCalEntry(
                        country = "Congo, Democratic Republic of the",
                        date = "ridiculus",
                        priority = "mus",
                        time = "penatibus",
                        title = "option",
                    ),
                ),
            ),
        ),
    )
}
