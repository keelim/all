package com.keelim.setting.screen.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {

        }
    }
}


@Preview
@Composable
private fun PreviewAdminScreen() {
    AdminScreen()
}
