package com.keelim.mygrade.ui.screen.word.write

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.util.trace
import com.keelim.composeutil.component.box.ReadyServiceBox

@Composable
fun WordWriteRoute() = trace("WordWriteScreen") {
    WordWriteScreen()
}

@Composable
private fun WordWriteScreen(
    viewModel: WordWriteViewModel = hiltViewModel(),
) = trace("WordWriteScreen") {
    ReadyServiceBox()
}

@Preview
@Composable
fun PreviewWordWriteScreen() {
    WordWriteScreen()
}
