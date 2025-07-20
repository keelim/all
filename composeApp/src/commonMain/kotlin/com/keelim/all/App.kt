package com.keelim.all

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun App() {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        ProjectIntroPage()
    }
}
