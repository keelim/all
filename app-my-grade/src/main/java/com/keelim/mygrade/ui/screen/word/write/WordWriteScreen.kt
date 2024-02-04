package com.keelim.mygrade.ui.screen.word.write

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.trace
import androidx.hilt.navigation.compose.hiltViewModel
import com.keelim.composeutil.component.box.ReadyServiceBox

@Composable
fun WordWriteRoute(
    viewModel: WordWriteViewModel = hiltViewModel(),
) = trace("WordWriteScreen") {
    WordWriteScreen()
}

@Composable
private fun WordWriteScreen() = trace("WordWriteScreen") {
    ReadyServiceBox()
}

@Preview
@Composable
fun PreviewWordWriteScreen() {
    WordWriteScreen()
}
