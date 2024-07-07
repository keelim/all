package com.keelim.nandadiagnosis.ui.screen.nutrients

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.keelim.composeutil.component.appbar.NavigationBackArrowBar
import com.keelim.composeutil.component.layout.EmptyView
import com.keelim.composeutil.component.layout.Loading
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space4

@Composable
fun NutrientRoute(
    onNutrientClick: (String, String) -> Unit,
    onNutrientTimerClick: () -> Unit,
    viewModel: NutrientViewModel = hiltViewModel(),
) = trace("NutrientRoute") {
    val uiState by viewModel.state.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current,
    )

    NutrientScreen(
        uiState = uiState,
        onNutrientClick = onNutrientClick,
        onNutrientTimerClick = onNutrientTimerClick,
    )
}

@Composable
private fun NutrientScreen(
    uiState: NutrientState,
    onNutrientClick: (String, String) -> Unit,
    onNutrientTimerClick: () -> Unit,
) = trace("NutrientScreen") {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNutrientTimerClick,
            ) {
                Icon(
                    imageVector = Icons.Filled.Call,
                    contentDescription = null,
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
        ) {
            NavigationBackArrowBar(title = "Search Nutrient")
            NutrientStateView(uiState = uiState, onNutrientClick = onNutrientClick)
        }
    }
}

@Composable
private fun NutrientStateView(uiState: NutrientState, onNutrientClick: (String, String) -> Unit) =
    trace("NutrientStateView") {
        when (uiState) {
            NutrientState.Error,
            NutrientState.Empty,
            -> EmptyView()

            NutrientState.Loading -> Loading()
            is NutrientState.Success -> {
                LazyColumn {
                    items(uiState.items) { (title, uri) ->
                        NutrientCard(title = title, uri = uri, onNutrientClick = { onNutrientClick(title, uri) })
                        Spacer(modifier = Modifier.height(space4))
                    }
                }
            }
        }
    }

@Preview(showBackground = true)
@Composable
fun PreviewNutrientScreen() {
    NutrientScreen(
        uiState = NutrientState.Empty,
        onNutrientClick = { _, _ -> },
        onNutrientTimerClick = {},
    )
}

@Composable
private fun NutrientCard(title: String, uri: String, onNutrientClick: () -> Unit) = trace("NutrientCard") {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier =
            Modifier.widthIn(max = 400.dp)
                .clip(MaterialTheme.shapes.large)
                .clickable { onNutrientClick() }
                .padding(space16),
        ) {
            Card(Modifier.fillMaxWidth()) {
                Box {
                    AsyncImage(
                        model =
                        "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1024&q=80",
                        modifier =
                        Modifier.clip(MaterialTheme.shapes.medium).aspectRatio(16 / 9f).fillMaxWidth(),
                        contentScale = ContentScale.Crop,
                        contentDescription = "null",
                    )
                    Button(
                        onClick = { onNutrientClick() },
                        modifier = Modifier.align(Alignment.TopEnd).padding(space16),
                        colors =
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                        ),
                    ) {
                        Icon(Icons.Outlined.Favorite, contentDescription = "Favorite")
                    }
                }
            }
            Spacer(Modifier.height(space12))
            Row(horizontalArrangement = Arrangement.spacedBy(space12)) {
                Column(verticalArrangement = Arrangement.spacedBy(space4)) {
                    Text(title, maxLines = 1)
                    // Row(horizontalArrangement = Arrangement.spacedBy(space8)) {
                    //   Text("4.5")
                    //   Icon(Icons.Rounded.Star, contentDescription = null, tint = Color(0xFFFF9800))
                    // }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewNutrientCard() {
    NutrientCard(
        title = "ridiculus",
        uri = "habemus",
        onNutrientClick = {},
    )
}
