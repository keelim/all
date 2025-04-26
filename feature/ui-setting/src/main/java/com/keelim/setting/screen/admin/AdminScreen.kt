package com.keelim.setting.screen.admin

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.trace
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.keelim.composeutil.component.appbar.NavigationBackArrowBar
import com.keelim.composeutil.resource.space8

@Composable
fun AdminRoute(
    viewModel: AdminViewModel = hiltViewModel(),
) = trace("AdminRoute") {
    AdminScreen()
}

@Composable
fun AdminScreen() = trace("AdminScreen") {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = space8),
    ) {
        NavigationBackArrowBar(title = "알림 확인")
        Spacer(
            modifier = Modifier.height(space8),
        )
        val context = LocalContext.current
        LazyColumn {
            item {
                SchemeTestSection(
                    onClick = { uri ->
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                uri.toUri(),
                            ),
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAdminScreen() {
    AdminScreen()
}
