package com.keelim.arducon.ui.screen.saastatus.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.component.layout.Loading
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space24

@Composable
fun SaastatusRoute(
    onRegister: () -> Unit,
    viewModel: SaastatusViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    SaastatusScreen(
        state = state,
        onRegister = onRegister,
    )
}

@Composable
fun SaastatusScreen(
    state: SaastatusState,
    onRegister: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = space16, vertical = space12),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Saastatus",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )
        }
        Spacer(
            modifier = Modifier.width(space12),
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(
            modifier = Modifier.width(space24),
        )
        when (state) {
            SaastatusState.Loading -> Loading()
            is SaastatusState.Success -> {
                if (state.items.isEmpty()) {
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
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSaastatusScreen() {
    SaastatusScreen(
        state = SaastatusState.Success(listOf()),
        onRegister = {},
    )
}

