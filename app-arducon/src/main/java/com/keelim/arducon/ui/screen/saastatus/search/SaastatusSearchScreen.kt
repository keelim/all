package com.keelim.arducon.ui.screen.saastatus.search

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.keelim.arducon.ui.screen.saastatus.SaastatusColumn

@Composable
fun SaastatusSearchRoute(
    viewModel: SaastatusSearchViewModel = hiltViewModel(),
) {
    SaastatusSearchScreen(

    )
}

@Composable
fun SaastatusSearchScreen() {
    SaastatusColumn {

    }
}



@Preview
@Composable
private fun PreviewSaastatusSearchScreen() {
    SaastatusSearchScreen()
}

