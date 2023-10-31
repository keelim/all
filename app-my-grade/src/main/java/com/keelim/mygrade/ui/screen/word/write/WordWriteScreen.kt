package com.keelim.mygrade.ui.screen.word.write

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.keelim.composeutil.component.box.ReadyServiceBox

@Composable
fun WordWriteRoute() {
    WordWriteScreen()
}

@Composable
private fun WordWriteScreen(
    viewModel: WordWriteViewModel = hiltViewModel()
) {
    ReadyServiceBox()
}

@Preview
@Composable
private fun PreviewWordWriteScreen() {
    WordWriteScreen()
}
