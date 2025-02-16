package com.keelim.setting.screen.admin

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.trace
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AdminRoute(
    viewModel: AdminViewModel = hiltViewModel(),
) = trace("AdminRoute") {
    AdminScreen()
}

@Composable
fun AdminScreen(
) = trace("AdminScreen") {
}

@Preview
@Composable
private fun PreviewAdminScreen() {
    AdminScreen()
}
