package com.keelim.mygrade.ui.screen.word.show

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.keelim.composeutil.component.box.ReadyServiceBox

@Composable
fun WordShowRoute(
    onWordWriteNavigate: () -> Unit,
) {
    WordShowScreen(
        onWordWriteNavigate = onWordWriteNavigate,
    )
}

@Composable
private fun WordShowScreen(
    onWordWriteNavigate: () -> Unit,
    viewModel: WordShowViewModel = hiltViewModel(),
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onWordWriteNavigate,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null,
                )
            }
        },
    ) { paddingValues ->
        ReadyServiceBox(
            modifier = Modifier.padding(paddingValues),
        )
    }
}

@Preview
@Composable
fun PreviewWordShowScreen() {
    WordShowScreen(
        onWordWriteNavigate = {},
    )
}
