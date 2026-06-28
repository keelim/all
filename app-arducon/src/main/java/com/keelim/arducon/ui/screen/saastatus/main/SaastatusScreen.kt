package com.keelim.arducon.ui.screen.saastatus.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.arducon.ui.screen.saastatus.SaastatusColumn
import com.keelim.composeutil.resource.space12

@Composable
fun SaastatusRoute(
    onRegister: () -> Unit,
    viewModel: SaastatusViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    SaastatusScreen(
        items = state,
        onRegister = onRegister,
    )
}

@Composable
fun SaastatusScreen(
    items: List<SaastatusItem>,
    onRegister: () -> Unit,
) {
    SaastatusColumn {
        if (items.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
            ) {
                SaastatusEmpty(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(space12),
                    onRegister = onRegister,
                )
            }
        } else {
            // not supported
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSaastatusScreen() {
    SaastatusScreen(
        items = listOf(),
        onRegister = {},
    )
}
