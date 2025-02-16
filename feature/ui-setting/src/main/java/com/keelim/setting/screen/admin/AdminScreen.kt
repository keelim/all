package com.keelim.setting.screen.admin

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.trace
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.core.net.toUri

@Composable
fun AdminRoute(
    viewModel: AdminViewModel = hiltViewModel(),
) = trace("AdminRoute") {
    AdminScreen()
}

@Composable
fun AdminScreen(
) = trace("AdminScreen") {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            SchemeTestSection(
                onClick = { uri ->
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            uri.toUri()
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Preview
@Composable
private fun PreviewAdminScreen() {
    AdminScreen()
}
