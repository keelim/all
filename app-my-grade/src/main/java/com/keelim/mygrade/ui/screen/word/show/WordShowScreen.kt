package com.keelim.mygrade.ui.screen.word.show

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import com.keelim.core.designsystem.component.KuiFloatingActionButton
import com.keelim.core.designsystem.component.KuiIcon
import com.keelim.core.designsystem.component.KuiScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.trace
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.keelim.composeutil.component.box.ReadyServiceBox

@Composable
fun WordShowRoute(
    onWordWriteNavigate: () -> Unit,
    viewModel: WordShowViewModel = hiltViewModel(),
) = trace("WordShowRoute") {
    WordShowScreen(
        onWordWriteNavigate = onWordWriteNavigate,
    )
}

@Composable
private fun WordShowScreen(
    onWordWriteNavigate: () -> Unit,
) = trace("WordShowScreen") {
    KuiScaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            KuiFloatingActionButton(
                onClick = onWordWriteNavigate,
            ) {
                KuiIcon(
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
