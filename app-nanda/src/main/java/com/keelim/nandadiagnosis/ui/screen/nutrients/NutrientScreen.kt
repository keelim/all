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
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.keelim.composeutil.component.appbar.NavigationBackArrowBar
import com.keelim.composeutil.component.layout.EmptyView
import com.keelim.composeutil.component.layout.Loading

@Composable
fun NutrientRoute(onNutrientClick: (String, String) -> Unit) {
    NutrientScreen(onNutrientClick = onNutrientClick)
}

@Composable
private fun NutrientScreen(
    viewModel: NutrientViewModel = hiltViewModel(),
    onNutrientClick: (String, String) -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    Column {
        NavigationBackArrowBar(title = "Search Nutrient")
        NutrientStateView(uiState = uiState, onNutrientClick = onNutrientClick)
    }
}

@Composable
private fun NutrientStateView(uiState: NutrientState, onNutrientClick: (String, String) -> Unit) {
    when (uiState) {
        NutrientState.Error,
        NutrientState.Empty,
        -> EmptyView()
        NutrientState.Loading -> Loading()
        is NutrientState.Success -> {
            LazyColumn {
                items(uiState.items) { (title, uri) ->
                    NutrientCard(title = title, uri = uri, onNutrientClick = { onNutrientClick(title, uri) })
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewNutrientScreen() {
    NutrientScreen(onNutrientClick = { _, _ -> })
}

@Preview(showBackground = true)
@Composable
private fun NutrientCard(title: String, uri: String, onNutrientClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier =
            Modifier.widthIn(max = 400.dp)
                .clip(MaterialTheme.shapes.large)
                .clickable { onNutrientClick() }
                .padding(16.dp),
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
                        modifier = Modifier.align(Alignment.TopEnd).padding(16.dp),
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
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(title, maxLines = 1)
                    // Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    //   Text("4.5")
                    //   Icon(Icons.Rounded.Star, contentDescription = null, tint = Color(0xFFFF9800))
                    // }
                }
            }
        }
    }
}
