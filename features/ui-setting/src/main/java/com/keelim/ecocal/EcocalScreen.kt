package com.keelim.ecocal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
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
    SealedUiState.Loading -> EmptyView()
    is SealedUiState.Success -> EcocalSuccessSection(state.value)
  }
}

@Composable
fun EcocalSuccessSection(entries: EcocalEntries) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(entries.entries) {entry ->
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 8.dp)
            ) {
                Text(text = entry.toString())
            }
            Divider(
                thickness = 1.dp
            )
        }
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
                              title = "option")))))
}
