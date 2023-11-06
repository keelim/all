package com.keelim.setting.screen.event

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun EventRoute() {
    EventScreen()
}

@Composable
fun EventScreen(viewModel: EventViewModel = hiltViewModel()) {
}

@Preview(showBackground = true)
@Composable
private fun PreviewEventScreen() {
    EventScreen()
}
